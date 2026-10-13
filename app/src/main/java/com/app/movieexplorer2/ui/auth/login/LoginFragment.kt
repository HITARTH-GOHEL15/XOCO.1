package com.app.movieexplorer2.ui.auth.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.movieexplorer2.R
import com.app.movieexplorer2.databinding.FragmentLoginBinding
import com.app.movieexplorer2.ui.viewmodel.AuthViewModel
import com.app.movieexplorer2.utils.resource.ResponseHandler.Companion.TAG
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.core.content.edit

@AndroidEntryPoint
class LoginFragment() : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    private lateinit var adapter: LoginAdapter

    private lateinit var auth: FirebaseAuth

    private lateinit var credentialManager: CredentialManager



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoginUser()
        setupRecycleView()

        credentialManager = CredentialManager.create(requireContext())

        auth = FirebaseAuth.getInstance()

        binding.loginTopBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecycleView() {
        adapter = LoginAdapter(

            onLoginClick = { requestPayload ->
                viewModel.LoginUser(
                    email = requestPayload.email,
                    password = requestPayload.password
                )
            },
            onRegisterClick = {
                findNavController().navigate(
                    com.app.movieexplorer2.R.id.action_loginFragment_to_registerFragment
                )
            },
            onGoogleLoginClick = {
                signIn()
            }
        )
        binding.loginRecycleView.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            this.adapter = this@LoginFragment.adapter
        }
    }


    private fun observeLoginUser() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {
                 viewModel.loginState.collect { state ->

                     when {

                         state.isLoading -> {

                         }

                         state.success -> {
                             val response = state.data

                             if (response == null || response.access.isBlank()) {
                                 Toast.makeText(
                                     requireContext(),
                                     "Access token missing",
                                     Toast.LENGTH_SHORT
                                 ).show()

                                 viewModel.resetLoginState()
                                 return@collect
                             }

                             requireContext()
                                 .getSharedPreferences(
                                     "auth_session",
                                     Context.MODE_PRIVATE
                                 )
                                 .edit {
                                     putString("access_token", response.access)
                                 }

                             Toast.makeText(
                                 requireContext(),
                                 "User login successful",
                                 Toast.LENGTH_SHORT
                             ).show()

                             viewModel.resetLoginState()

                             findNavController().navigate(
                                 R.id.action_loginFragment_to_homeFragment,
                                 null,
                                 androidx.navigation.navOptions {
                                     popUpTo(R.id.onboardingFragment) {
                                         inclusive = true
                                     }
                                     launchSingleTop = true
                                 }
                             )
                         }

                         state.error != null -> {
                             Toast.makeText(
                                 requireContext(),
                                 state.error.errorMessage ?: "User Login failed",
                                 Toast.LENGTH_SHORT
                             ).show()
                         }
                     }
                 }
            }
        }
    }

    private fun signIn() {
        // Create the dialog configuration for the Credential Manager request
        val signInWithGoogleOption = GetSignInWithGoogleOption
            .Builder(serverClientId = requireContext().getString(R.string.client_id))
            .build()

        // Create the Credential Manager request using the configuration created above
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        launchCredentialManager(request)
    }

    private fun showBottomSheet() {
        // Create the bottom sheet configuration for the Credential Manager request
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(requireContext().getString(com.app.movieexplorer2.R.string.client_id))
            .build()

        // Create the Credential Manager request using the configuration created above
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        launchCredentialManager(request)
    }

    private fun launchCredentialManager(request: GetCredentialRequest) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Launch Credential Manager UI
                val result = credentialManager.getCredential(
                    context = requireContext(),
                    request = request
                )

                createGoogleIdToken(result.credential)
            } catch (e: GetCredentialException) {
                Log.e(TAG, "Couldn't retrieve user's credentials: ${e.localizedMessage}")
            }
        }
    }

    private fun createGoogleIdToken(credential: Credential) {
        // Check if credential is of type Google ID
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.w(TAG, "Credential is not of type Google ID!")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if(task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d(TAG, "Authentication successful: ${user?.displayName}")
                    setOf(
                        Toast.makeText(
                            requireContext(),
                            "User Login successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    )
                    findNavController().navigate(
                        R.id.action_loginFragment_to_homeFragment
                    )
                } else {
                    Log.e(TAG, "Authentication failed: ${task.exception?.message}")
                    Toast.makeText(
                        requireContext(),
                        "User Login failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}