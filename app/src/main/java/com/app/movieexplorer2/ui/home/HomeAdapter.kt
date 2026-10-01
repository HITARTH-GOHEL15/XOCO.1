package com.app.movieexplorer2.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.movieexplorer2.data.models.HomeItem
import com.app.movieexplorer2.data.models.Movie
import com.app.movieexplorer2.databinding.HeaderLayoutBinding
import com.app.movieexplorer2.databinding.ItemHeaderBinding
import com.app.movieexplorer2.databinding.ItemMovieSectionBinding
import com.app.movieexplorer2.ui.movie.MovieAdapter
import com.app.movieexplorer2.ui.viewPager.HeaderViewPagerAdapter
import kotlin.math.log


class HomeAdapter(
    private val onMovieClick: (Movie) -> Unit
) : androidx.recyclerview.widget.ListAdapter<HomeItem, HomeAdapter.HomeViewHolder>(
    HomeDiffCallback()
) {
    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_SECTION = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeItem.Header ->
                VIEW_TYPE_HEADER

            is HomeItem.MovieSection ->
                VIEW_TYPE_SECTION
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeViewHolder {

        return when (viewType) {

            VIEW_TYPE_HEADER -> {

                val binding = HeaderLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return HeaderViewHolder(
                    binding
                )
            }

            VIEW_TYPE_SECTION -> {

                val binding = ItemMovieSectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                return SectionViewHolder(
                    binding,
                    onMovieClick
                )
            }

            else -> {
                error("Unknown ViewType: $viewType")
            }


        }
    }

    override fun onBindViewHolder(
        holder: HomeViewHolder,
        position: Int
    ) {
        when (val item = getItem(position)) {

            is HomeItem.Header -> {
                (holder as HeaderViewHolder).bind(item)
            }

            is HomeItem.MovieSection -> {
                (holder as SectionViewHolder).bind(item)
            }
        }

    }

    abstract class HomeViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView)

    class HeaderViewHolder(
        private val binding: HeaderLayoutBinding
    ) : HomeViewHolder(binding.root) {
        fun bind(item: HomeItem.Header) {
            binding.featuredPager.adapter = HeaderViewPagerAdapter(item.movies)
        }

    }

    class SectionViewHolder(
        private val binding: ItemMovieSectionBinding,
        private val onMovieClick: (Movie) -> Unit
    ) : HomeViewHolder(binding.root) {

        private val movieAdapter = MovieAdapter(onMovieClick)

        init {

            binding.movieRecycleView.apply {

                layoutManager =
                    GridLayoutManager(
                        context,
                        1,
                        RecyclerView.HORIZONTAL,
                        false
                    )

                adapter = movieAdapter
            }
        }
        fun bind(item: HomeItem.MovieSection) {
            binding.sectionTitle.text =
                item.title

            movieAdapter.submitList(item.movies)

        }
    }

}



