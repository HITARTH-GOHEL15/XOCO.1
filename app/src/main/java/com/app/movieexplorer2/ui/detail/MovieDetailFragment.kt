package com.app.movieexplorer2.ui.detail

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.movieexplorer2.R
import com.app.movieexplorer2.data.remote.MovieDto
import com.app.movieexplorer2.databinding.FragmentDetailBinding
import com.app.movieexplorer2.ui.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailAdapter: MovieDetailAdapter

    private val viewModel: MovieDetailViewModel by viewModels()

    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            FragmentDetailBinding.inflate(
                inflater,
                container,
                false
            )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        hideActivityToolbar()

        setupToolbar()
        setupRecyclerView()
        observeUiState()

        viewModel.getMovieDetails(
            args.movieId
        )
    }

    private fun hideActivityToolbar() {

        requireActivity()
            .findViewById<View>(R.id.topAppBar)
            ?.visibility = View.GONE
    }

    private fun showActivityToolbar() {

        requireActivity()
            .findViewById<View>(R.id.topAppBar)
            ?.visibility = View.VISIBLE
    }

    private fun setupToolbar() {

        binding.detailTopBar.setNavigationOnClickListener {

            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {

        detailAdapter =
            MovieDetailAdapter()

        binding.detailRecycleView.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            adapter =
                detailAdapter

            setHasFixedSize(false)
        }
    }

    private fun observeUiState() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.uiState.collect { state ->

                    when {

                        state.isLoading -> {
                            showLoading()
                        }

                        state.error != null -> {
                            showError(
                                state.error.errorMessage
                                    ?: "Unable to load movie"
                            )
                        }

                        state.movie != null -> {
                            showMovie(
                                state.movie
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showMovie(movie: MovieDto) {

        hideLoading()

        detailAdapter.submitMovie(movie)
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

        binding.errorContainer.visibility =
            View.GONE
    }

    private fun showError(message: String) {

        binding.progressBar.visibility =
            View.GONE

        binding.errorContainer.visibility =
            View.VISIBLE

        binding.errorText.text =
            message
    }

    override fun onDestroyView() {

        binding.detailRecycleView.adapter = null

        showActivityToolbar()

        _binding = null

        super.onDestroyView()
    }
}