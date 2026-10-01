package com.app.movieexplorer2.utils.resource

import android.content.Context
import com.app.movieexplorer2.R
import com.app.movieexplorer2.data.remote.MovieResponseDto
import com.app.movieexplorer2.utils.ErrorBean
import com.app.movieexplorer2.utils.ErrorUtils
import retrofit2.Response
import java.net.SocketTimeoutException

class ResponseHandler(
    private val context: Context,
    private val errorUtils: ErrorUtils
) {

    companion object {
        const val TAG = "ResponseHandler"
    }

    fun <T> handleResponse(
        response: Response<T>,
        isAutoHandleError: Boolean = true
    ): Resource<T> {

        if (response.isSuccessful) {

            val responseBody = response.body()

            return if (response.code() == 204) {

                Resource.success(null)

            } else {

                if (responseBody != null) {

                    Resource.success(responseBody)

                } else {

                    Resource.error(
                        errorUtils.handleError(
                            IllegalStateException(
                                context.getString(
                                    R.string.unexpected_response
                                )
                            ),
                            ""
                        )
                    )
                }
            }
        }

        return when (response.code()) {

            400, 422 -> {

                handleClientError(
                    response,
                    isAutoHandleError
                )
            }

            401 -> {

                Resource.error(
                    ErrorBean(
                        errorCode = 401,
                        errorMessage = "Unauthorized"
                    )
                )
            }

            403 -> {

                Resource.error(
                    ErrorBean(
                        errorCode = 403,
                        errorMessage = "Access denied"
                    )
                )
            }

            404 -> {

                Resource.error(
                    ErrorBean(
                        errorCode = 404,
                        errorMessage = "Movie data not found"
                    )
                )
            }

            408 -> {

                Resource.error(
                    errorUtils.handleError(
                        SocketTimeoutException(),
                        ""
                    )
                )
            }

            500, 502, 503, 504 -> {

                Resource.error(
                    ErrorBean(
                        errorCode = response.code(),
                        errorMessage = context.getString(
                            R.string.something_went_wrong
                        )
                    )
                )
            }

            else -> {

                Resource.error(
                    errorUtils.handleError(
                        null,
                        ""
                    )
                )
            }
        }
    }

    private fun <T> handleClientError(
        response: Response<T>,
        isAutoHandleError: Boolean
    ): Resource<T> {

        val errorBody = response.errorBody()?.string()

        if (!errorBody.isNullOrEmpty()) {

            return if (isAutoHandleError) {

                Resource.error(
                    errorUtils.handleMapError(
                        errorBody
                    )
                )

            } else {

                Resource.error(
                    ErrorBean(
                        errorCode = response.code(),
                        error = errorBody
                    )
                )
            }
        }

        return Resource.error(
            errorUtils.handleError(
                null,
                ""
            )
        )
    }

    fun <T> handleException(
        e: Throwable?
    ): Resource<T> {

        return Resource.error(
            errorUtils.handleError(
                e,
                ""
            )
        )
    }
}
