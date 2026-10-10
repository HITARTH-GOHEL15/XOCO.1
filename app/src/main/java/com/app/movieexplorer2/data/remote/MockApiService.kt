package com.app.movieexplorer2.data.remote

import com.app.movieexplorer2.data.remote.models.MockPostResponse
import com.app.movieexplorer2.data.remote.models.UpdatePostRequest
import com.app.movieexplorer2.data.remote.models.UpdateReviewPatchRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path

interface MockApiService {

    @PUT("posts/{id}")
    suspend fun putMovie(
        @Path("id") id:Int,
        @Body request: UpdatePostRequest
    ): Response<MockPostResponse>

    @PATCH("posts/{id}")
    suspend fun patchMovie(
        @Path("id") id: Int,
        @Body request: UpdateReviewPatchRequest
    ): Response<MockPostResponse>
}