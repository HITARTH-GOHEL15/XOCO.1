package com.app.movieexplorer2.ui.home


import androidx.recyclerview.widget.DiffUtil
import com.app.movieexplorer2.data.models.HomeItem

class HomeDiffCallback : DiffUtil.ItemCallback<HomeItem>() {
    override fun areItemsTheSame(
        oldItem: HomeItem,
        newItem: HomeItem
    ): Boolean {
        return when {
            oldItem is HomeItem.Header &&
                    newItem is HomeItem.Header -> true

            oldItem is HomeItem.MovieSection &&
                    newItem is HomeItem.MovieSection ->
                oldItem.id == newItem.id


            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: HomeItem,
        newItem: HomeItem
    ): Boolean {
        return oldItem == newItem
    }

}