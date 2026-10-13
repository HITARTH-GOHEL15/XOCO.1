package com.app.movieexplorer2.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.movieexplorer2.R
import com.app.movieexplorer2.databinding.FragmentOnboardingThirdBinding

class OnboardingThirdFragment : Fragment() {
    private var _binding: FragmentOnboardingThirdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.GetStartedButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_onboardingFragment_to_authTypesFragment
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}