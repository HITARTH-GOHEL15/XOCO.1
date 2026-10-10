package com.app.movieexplorer2.utils

import java.util.Locale

fun Double.toRatingText(): String = String.format(Locale.US, "%.1f", this)