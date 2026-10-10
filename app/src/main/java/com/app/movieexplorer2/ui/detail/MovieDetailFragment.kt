package com.app.movieexplorer2.ui.detail

import android.os.Bundle
import android.util.Log
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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.movieexplorer2.data.remote.models.UpdatePostRequest
import com.app.movieexplorer2.data.remote.models.UpdateReviewPatchRequest
import com.app.movieexplorer2.databinding.FragmentDetailBinding
import com.app.movieexplorer2.ui.helper.showAddReviewDialog
import com.app.movieexplorer2.ui.viewmodel.MovieDetailViewModel
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch



@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailViewModel by viewModels()

    private val args: MovieDetailFragmentArgs by navArgs()

    private lateinit var detailAdapter: MovieDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply { duration = 300 }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply { duration = 300 }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailBinding.inflate(
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
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()

        observeMovieDetails()
        observeRating()
        observePutMovie()
        observePatchMovie()

        viewModel.getMovieDetails(args.movieId)

        /*
         * Only call this if your ViewModel already has
         * createGuestSession().
         */
        viewModel.createGuestSession()
    }

    private fun setupToolbar() {

        binding.detailTopBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {

        detailAdapter = MovieDetailAdapter(

            onAddReviewClick = {

                showAddReviewDialog(requireContext()) { rating, review ->

                    submitRating(rating)

                    /*
                     * 'review' is currently not sent to TMDB.
                     *
                     * TMDB's rating endpoint only accepts
                     * the numeric rating value.
                     */
                }
            },
            onPutTestClick = {
                val request = UpdatePostRequest(
                    userId = 1,
                    title = "Updated Movie Review",
                    body = "This review was updated using PUT"
                )

                viewModel.putMovie(
                    id = 1,
                    request = request
                )
            },
            onPatchTestClick = {
                val request = UpdateReviewPatchRequest(
                    body = "This review was updated using PATCH"
                )

                viewModel.patchMovie(

                )
            }
        )

        binding.detailRecycleView.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            adapter = detailAdapter

            overScrollMode = View.OVER_SCROLL_NEVER
        }
    }

    private fun submitRating(rating: Float) {

        /*
         * RatingBar has 5 stars.
         *
         * TMDB expects 0.5 - 10.
         *
         * Therefore:
         *
         * 1 star = 2
         * 2 stars = 4
         * 3 stars = 6
         * 4 stars = 8
         * 5 stars = 10
         */

        if (rating <= 0f) {

            Toast.makeText(
                requireContext(),
                "Please select a rating",
                Toast.LENGTH_SHORT
            ).show()

            return
        }
        val tmdbRating =
            (rating * 2).toDouble()

        viewModel.rateMovie(
            movieId = args.movieId,
            rating = tmdbRating
        )
    }

    private fun observeMovieDetails() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.uiState.collect { state ->

                    Log.d(
                        "DETAIL_UI",
                        "isLoading=${state.isLoading}, movieLoaded=${state.movieLoaded}, error=${state.error}"
                    )

                    when {

                        state.isLoading -> {

                            binding.progressBar.visibility =
                                View.VISIBLE
                        }

                        state.error != null -> {

                            binding.progressBar.visibility =
                                View.GONE

                            Toast.makeText(
                                requireContext(),
                                state.error.errorMessage
                                    ?: "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        state.movieLoaded -> {
                            binding.progressBar.visibility = View.GONE

                            Log.d(
                                "DETAIL_UI",
                                "ProgressBar visibility after GONE = ${binding.progressBar.visibility}"
                            )

                            state.movie?.let { movie ->
                                Log.d("DETAIL_UI", "Submitting movie to adapter = ${movie.title}")
                                detailAdapter.submitMovie(movie)

                                Log.d(
                                    "DETAIL_UI",
                                    "Adapter itemCount = ${detailAdapter.itemCount}"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observePutMovie() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.putState.collect { state ->

                when {
                    state.isLoading -> {
                        // Show loading if you want
                    }

                    state.success -> {
                        Toast.makeText(
                            requireContext(),
                            "Movie updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d(
                            "PUT_MOVIE",
                            "Updated movie = ${state.data}"
                        )
                    }

                    state.error != null -> {
                        Toast.makeText(
                            requireContext(),
                            state.error?.errorMessage
                                ?: "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun observePatchMovie() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.patchUiState.collect { state ->

                when {

                    state.isLoading -> {
                        // Show loading if you want
                    }

                    state.success -> {

                        Toast.makeText(
                            requireContext(),
                            "Movie review patched successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d(
                            "PATCH_MOVIE",
                            "Patched movie = ${state.data}"
                        )
                    }

                    state.error != null -> {

                        Toast.makeText(
                            requireContext(),
                            state.error?.errorMessage
                                ?: "Something went wrong",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    private fun observeRating() {

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(
                Lifecycle.State.STARTED
            ) {

                viewModel.rateState.collect { state ->

                    when {

                        state.isLoading -> {
                            // Optional:
                            // show rating progress here
                        }

                        state.success -> {

                            Toast.makeText(
                                requireContext(),
                                "Rating submitted successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        state.error != null -> {

                            Toast.makeText(
                                requireContext(),
                                state.error.errorMessage
                                    ?: "Failed to submit rating",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {

        binding.detailRecycleView.adapter = null

        _binding = null

        super.onDestroyView()
    }
}