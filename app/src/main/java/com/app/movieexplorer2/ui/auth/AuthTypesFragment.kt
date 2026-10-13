package com.app.movieexplorer2.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.movieexplorer2.R
import com.app.movieexplorer2.databinding.FragmentAuthTypesBinding

class AuthTypesFragment : Fragment() {

    private var _binding: FragmentAuthTypesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthTypesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emailAuthButton.setOnClickListener {
            findNavController().navigate(R.id.action_authTypesFragment_to_loginFragment)
        }
        binding.phoneAuthButton.setOnClickListener {
            findNavController().navigate(R.id.action_authTypesFragment_to_phoneAuthFragment)
        }
        binding.authTypesTopBar.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}