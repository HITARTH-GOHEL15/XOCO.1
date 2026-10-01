package com.app.movieexplorer2.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.movieexplorer2.R
import com.app.movieexplorer2.data.models.HomeItem
import com.app.movieexplorer2.databinding.FragmentHomeBinding
import com.app.movieexplorer2.ui.MovieUiState
import com.app.movieexplorer2.ui.detail.MovieDetailFragment
import com.app.movieexplorer2.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeAdapter: HomeAdapter

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    private fun setupRecycleView() {

        homeAdapter = HomeAdapter { movie ->

            val movieId =
                movie.id?.toInt() ?: return@HomeAdapter

            val action =
                HomeFragmentDirections
                    .actionHomeFragmentToMovieDetailFragment(
                        movieId
                    )

            findNavController().navigate(action)
        }

        binding.homeRecycleView.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            adapter = homeAdapter

            setHasFixedSize(false)
        }
    }
    private fun observeUiState() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.uiState.collect { state ->

                    if (state.isLoading) {
                        showLoading()
                    } else {
                        hideLoading()
                    }

                    if (state.error != null) {

                        showError(
                            state.error.errorMessage
                                ?: "Unable to load movies"
                        )

                        return@collect
                    }

                    showMovies(state)
                }
            }
        }
    }

    private fun showMovies(
        state: MovieUiState
    ) {

        val homeItems = listOf(

            HomeItem.Header(
                movies = state.popularMovies
            ),

            HomeItem.MovieSection(
                id = "top_movies",
                title = "Top Movies",
                movies = state.topMovies
            ),

            HomeItem.MovieSection(
                id = "popular_movies",
                title = "Popular Movies",
                movies = state.popularMovies
            ),

            HomeItem.MovieSection(
                id = "upcoming_movies",
                title = "Upcoming Movies",
                movies = state.upcomingMovies
            )
        )

        homeAdapter.submitList(homeItems)
    }

    private fun showLoading() {

        binding.progressBar.visibility =
            View.VISIBLE

        binding.errorContainer.visibility =
            View.GONE
    }

    private fun hideLoading() {

        binding.progressBar.visibility =
            View.GONE
    }

    private fun showError(
        message: String
    ) {

        binding.progressBar.visibility =
            View.GONE

        binding.errorContainer.visibility =
            View.VISIBLE

        // If your error TextView has a different ID,
        // replace binding.errorMessage accordingly.
        binding.errorText.text = message
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(
            view,
            savedInstanceState
        )

        setupRecycleView()

        observeUiState()

        viewModel.getMovies()
    }

    override fun onDestroyView() {

        binding.homeRecycleView.adapter = null

        _binding = null

        super.onDestroyView()
    }
}