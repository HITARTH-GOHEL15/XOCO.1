package com.app.movieexplorer2.ui.helper

import android.content.Context
import android.view.LayoutInflater
import com.app.movieexplorer2.R
import com.app.movieexplorer2.databinding.DialogAddReviewBinding
import com.app.movieexplorer2.utils.toRatingText
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showAddReviewDialog(
    context: Context,
    onSubmit: (rating: Float, review: String) -> Unit
) {
    val b = DialogAddReviewBinding.inflate(LayoutInflater.from(context))
    val labels = context.resources.getStringArray(R.array.review_labels)
    val dialog = MaterialAlertDialogBuilder(context).setView(b.root).create()
    dialog.window?.setBackgroundDrawableResource(R.drawable.bg_dialog)
    b.submitButton.isEnabled = false
    b.reviewRating.setOnRatingBarChangeListener { _, rating, _ ->
        b.submitButton.isEnabled = rating > 0f
        b.reviewRatingLabel.text = if (rating > 0f) {
            val word = labels[kotlin.math.ceil(rating).toInt().coerceIn(1, 5) - 1]
            "${rating.toDouble().toRatingText()} · $word"
        } else {
            context.getString(R.string.review_rate_hint)
        }
    }
    b.cancelButton.setOnClickListener { dialog.dismiss() }
    b.submitButton.setOnClickListener {
        onSubmit(b.reviewRating.rating, b.reviewInput.text?.toString().orEmpty().trim())
        dialog.dismiss()
    }
    dialog.show()
}