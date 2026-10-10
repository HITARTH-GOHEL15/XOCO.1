package com.app.movieexplorer2.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.movieexplorer2.databinding.FragmentLoginBinding
import com.app.movieexplorer2.databinding.ItemLoginBinding
import com.app.movieexplorer2.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment() : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    private lateinit var adapter: LoginAdapter

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

                findNavController().navigate(
                    com.app.movieexplorer2.R.id.action_loginFragment_to_homeFragment
                )
            },
            onRegisterClick = {
                findNavController().navigate(
                    com.app.movieexplorer2.R.id.action_loginFragment_to_registerFragment
                )
            },
            onGoogleLoginClick = {
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
                             setOf(
                                 Toast.makeText(
                                     requireContext(),
                                     "User Login successfully",
                                     Toast.LENGTH_SHORT
                                 ).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}