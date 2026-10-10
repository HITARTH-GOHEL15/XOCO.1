package com.app.movieexplorer2.ui.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.movieexplorer2.R
import com.app.movieexplorer2.databinding.FragmentRegisterBinding
import com.app.movieexplorer2.ui.home.HomeFragmentDirections
import com.app.movieexplorer2.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    private lateinit var adapter: RegisterAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeRegisterUser()
        setupRecycleView()

        binding.registerTopBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun setupRecycleView() {
        adapter = RegisterAdapter(

            onRegisterClick = { requestPayload ->
                viewModel.registerUser(
                    fullName = requestPayload.fullName,
                    email = requestPayload.email,
                    password = requestPayload.password
                )

            },
            onGoogleRegisterClick = {

            },
            onLoginClick = {
                findNavController().navigate(
                    R.id.action_registerFragment_to_loginFragment
                )
            },
            onVerifyClick = {
                findNavController().navigate(
                    R.id.action_registerFragment_to_verifyFragment
                )
            }
        )

        binding.registerRecycleView.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            this.adapter = this@RegisterFragment.adapter
        }

    }

    private fun observeRegisterUser() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.registerState.collect { state ->

                    when {

                        state.isLoading -> {

                        }

                        state.success -> {

                            setOf(
                                Toast.makeText(
                                    requireContext(),
                                    "User Registered successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            )
                        }

                        state.error != null -> {

                            Toast.makeText(
                                requireContext(),
                                state.error.errorMessage
                                    ?: "User Registration failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {

        binding.registerRecycleView.adapter = null

        _binding = null

        super.onDestroyView()
    }
}