package com.app.movieexplorer2.ui.viewPager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import com.app.movieexplorer2.R
import com.app.movieexplorer2.data.models.Movie
import com.app.movieexplorer2.databinding.ItemHeaderBinding

class HeaderViewPagerAdapter(private val movies: List<Movie>) :
    RecyclerView.Adapter<HeaderViewPagerAdapter.ViewPagerHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerHolder {
        val binding = ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewPagerHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewPagerHolder,
        position: Int
    ) {
        holder.bind(movies[position])


    }

    override fun getItemCount(): Int {
        return movies.size
    }


    class ViewPagerHolder(val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.title

            val posterUrl =
                "https://image.tmdb.org/t/p/w500${movie.poster}"

            binding.posterImage.load(posterUrl)
        }
    }
}