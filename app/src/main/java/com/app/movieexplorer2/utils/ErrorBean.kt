package com.app.movieexplorer2.utils

import com.app.movieexplorer2.R

data class ErrorBean(
    var errorCode: Int = 0,
    val errorMessage: String? = "",
    val errorKey: String? = "",
    val errorTag: String? = null,
    val error: String? = null,
    val errorImage: Int = R.drawable.ic_parsing_error
)