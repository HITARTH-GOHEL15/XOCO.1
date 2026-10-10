package com.app.movieexplorer2.ui.auth.verifyOTP

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

    private lateinit var adapter: VerifyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerifyBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeVerifyUser()
        setupRecycleView()

        binding.verifyTopBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecycleView() {
        adapter = VerifyAdapter(
            onVerifyClick = { requestPayload ->
                viewModel.VerifyUser(
                    email = requestPayload.email,
                    code = requestPayload.otp
                )

                findNavController().navigate(
                    R.id.action_verifyFragment_to_homeFragment
                )
            }
        )
    }

    private fun observeVerifyUser() {
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
                                    "User verified successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            )
                        }

                        state.error != null -> {
                            Toast.makeText(
                                requireContext(),
                                state.error.errorMessage
                                    ?: "User verification failed",
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