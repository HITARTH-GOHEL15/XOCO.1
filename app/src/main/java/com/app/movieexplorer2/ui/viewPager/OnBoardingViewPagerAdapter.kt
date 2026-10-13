package com.app.movieexplorer2.ui.viewPager

import android.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnBoardingViewPagerAdapter(
    private val fragment: List<androidx.fragment.app.Fragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = fragment.size

    override fun createFragment(position: Int) = fragment[position]
}