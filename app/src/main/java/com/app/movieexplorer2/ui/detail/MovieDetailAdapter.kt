package com.app.movieexplorer2.ui.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.app.movieexplorer2.data.remote.MovieDto
import com.app.movieexplorer2.databinding.ItemMovieDetailBinding

class MovieDetailAdapter : RecyclerView.Adapter<MovieDetailAdapter.MovieDetailHolder>() {

    private var movie: MovieDto? = null

    @SuppressLint("NotifyDataSetChanged")
    fun submitMovie(movie: MovieDto) {

        this.movie = movie

        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieDetailHolder {
        val binding = ItemMovieDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MovieDetailHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MovieDetailHolder,
        position: Int
    ) {
        movie?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return if(movie != null) 1 else 0
    }


    class MovieDetailHolder(val binding: ItemMovieDetailBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieDto) {
            binding.movieTitle.text = movie.title
            binding.ratingText.text = "${movie.voteAverage}"
            binding.movieLang.text = movie.originalLanguage
            binding.movieReleaseDate.text = movie.releaseDate
            binding.movieOverview.text = movie.overview
            binding.movieRuntime.text = "${movie.runtime}"
            binding.movieRevenue.text = "${movie.revenue}"
            binding.moviePopularity.text = "${movie.popularity}"
            binding.movieStatus.text = movie.status
            binding.movieTagline.text = movie.tagline
            val posterUrl =
                "https://image.tmdb.org/t/p/w500${movie.posterPath}"
            binding.posterImageDetail.load(
                posterUrl
            )
        }
    }
}