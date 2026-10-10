package com.app.movieexplorer2.ui.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import com.app.movieexplorer2.R
import com.app.movieexplorer2.data.models.Movie
import com.app.movieexplorer2.databinding.ItemMovieBinding
import com.app.movieexplorer2.utils.toRatingText

class MovieAdapter(private val onMovieClick: (Movie) -> Unit) :
  ListAdapter<Movie, MovieAdapter.ItemMovieHolder>(
      MovieDiffCallback()
  ){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemMovieHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ItemMovieHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemMovieHolder,
        position: Int
    ) {
        holder.bind(getItem(position))

        holder.itemView.setOnClickListener {

            val currentPosition = holder.bindingAdapterPosition

            if (currentPosition != RecyclerView.NO_POSITION) {
                onMovieClick(
                    getItem(currentPosition)
                )
            }
        }
    }






    class ItemMovieHolder(val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.title
            val posterUrl =
                "https://image.tmdb.org/t/p/w500${movie.poster}"


            binding.posterImage.load(posterUrl)

            binding.ratingText.text = movie.rating?.toRatingText()
        }
    }
}