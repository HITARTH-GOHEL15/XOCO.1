package com.app.movieexplorer2.ui.auth.verifyOTP

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.app.movieexplorer2.R
import com.app.movieexplorer2.databinding.FragmentVerifyBinding
import com.app.movieexplorer2.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VerifyFragment : Fragment() {

    private var _binding: FragmentVerifyBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupVerifyButton()
        observeVerifyUser()

        binding.verifyTopBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupVerifyButton() {
        binding.verifyButton.setOnClickListener {
            val email = binding.emailTextField.editText
                ?.text.toString().trim()

            val code = binding.otpTextField.editText
                ?.text.toString().trim()

            binding.emailTextField.error = null
            binding.otpTextField.error = null

            if (email.isBlank()) {
                binding.emailTextField.error = "Email is required"
                return@setOnClickListener
            }

            if (code.isBlank()) {
                binding.otpTextField.error = "OTP is required"
                return@setOnClickListener
            }

            viewModel.VerifyUser(email, code)
        }
    }

    private fun observeVerifyUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.verifyState.collect { state ->
                    binding.verifyButton.isEnabled = !state.isLoading

                    when {
                        state.isLoading -> {
                            // Optionally show a loading indicator.
                        }

                        state.success -> {
                            val response = state.data

                            if (response == null || response.access.isBlank()) {
                                Toast.makeText(
                                    requireContext(),
                                    "Access token missing",
                                    Toast.LENGTH_SHORT
                                ).show()

                                viewModel.resetVerifyState()
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
                                "User Register successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            viewModel.resetLoginState()

                            findNavController().navigate(
                                R.id.action_verifyFragment_to_homeFragment,
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
                                state.error.errorMessage
                                    ?: "OTP verification failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}