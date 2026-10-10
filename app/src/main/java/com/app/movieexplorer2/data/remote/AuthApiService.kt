package com.app.movieexplorer2.data.remote

import com.app.movieexplorer2.data.remote.models.LoginRequest
import com.app.movieexplorer2.data.remote.models.LoginResponse
import com.app.movieexplorer2.data.remote.models.OtpVerifyRequest
import com.app.movieexplorer2.data.remote.models.OtpVerifyResponse
import com.app.movieexplorer2.data.remote.models.RegisterResponse
import com.app.xoco2.data.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/auth/login/")
    suspend fun userLogin(
        @Body loginRequest: LoginRequest
    ) : Response<LoginResponse>

    @POST("/api/auth/verify-otp/")
    suspend fun userVerification(
        @Body verifyRequest: OtpVerifyRequest
    ) : Response<OtpVerifyResponse>

    @POST("/api/auth/register/")
    suspend fun userRegistration(
        @Body registerRequest: RegisterRequest
    ) : Response<RegisterResponse>
}