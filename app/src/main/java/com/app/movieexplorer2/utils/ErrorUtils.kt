
package com.app.movieexplorer2.utils

import android.content.Context
import com.app.movieexplorer2.R
import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
import java.net.SocketTimeoutException

class ErrorUtils(
    private val context: Context
) {

    fun handleError(
        e: Throwable?,
        key: String?
    ): ErrorBean {

        return if (e != null) {

            when (e) {

                is IllegalStateException -> {

                    ErrorBean(
                        errorCode = 500,
                        errorMessage = e.localizedMessage,
                        errorKey = key,
                        errorImage =
                            R.drawable.ic_unexpected_response
                    )
                }

                is SocketTimeoutException -> {

                    ErrorBean(
                        errorCode = 408,
                        errorMessage =
                            context.getString(
                                R.string.connection_timeout
                            ),
                        errorKey = key,
                        errorImage =
                            R.drawable.ic_request_time_out
                    )
                }

                is ConnectException,
                is UnknownHostException -> {

                    ErrorBean(
                        errorCode = 0,
                        errorMessage =
                            context.getString(
                                R.string.no_internet
                            ),
                        errorKey = key,
                        errorImage =
                            R.drawable.ic_connectivity
                    )
                }

                is JsonParseException,
                is HttpException -> {

                    ErrorBean(
                        errorCode = 500,
                        errorMessage =
                            context.getString(
                                R.string.unexpected_response
                            ),
                        errorKey = key,
                        errorImage =
                            R.drawable.ic_unexpected_response
                    )
                }

                else -> {
                    handleMapError(
                        e.message ?: ""
                    )
                }
            }

        } else {

            ErrorBean(
                errorCode = 0,
                errorMessage =
                    context.getString(
                        R.string.unknown_error
                    ),
                errorKey = key
            )
        }
    }

    fun handleMapError(
        errorString: String
    ): ErrorBean {

        if (errorString.isEmpty()) {
            return ErrorBean(
                errorCode = 0,
                errorMessage =
                    context.getString(
                        R.string.something_went_wrong
                    ),
                errorKey = ""
            )
        }

        return ErrorBean(
            errorCode = 0,
            errorMessage = errorString,
            errorKey = ""
        )
    }
}
