package com.app.movieexplorer2.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.app.movieexplorer2.R
import com.app.movieexplorer2.databinding.FragmentOnboardingBinding
import com.app.movieexplorer2.ui.viewPager.OnBoardingViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewpagerAdapter: OnBoardingViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()
    }

    private fun setUpViewPager() {
        val fragments = listOf(
            OnboardingFirstFragment(),
            OnboardingSecondFragment(),
            OnboardingThirdFragment()
        )

        viewpagerAdapter = OnBoardingViewPagerAdapter(
            fragments,
            childFragmentManager,
            viewLifecycleOwner.lifecycle
        )

        binding.viewPager.adapter = viewpagerAdapter

        // Connect dots to ViewPager2
        binding.tabLayout.attachTo(binding.viewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}