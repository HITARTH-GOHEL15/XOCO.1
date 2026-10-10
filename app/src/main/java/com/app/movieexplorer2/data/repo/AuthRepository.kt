package com.app.movieexplorer2.data.repo

import com.app.movieexplorer2.data.remote.AuthApiService
import com.app.movieexplorer2.data.remote.models.LoginRequest
import com.app.movieexplorer2.data.remote.models.LoginResponse
import com.app.movieexplorer2.data.remote.models.OtpVerifyRequest
import com.app.movieexplorer2.data.remote.models.OtpVerifyResponse
import com.app.movieexplorer2.data.remote.models.RegisterResponse
import com.app.movieexplorer2.utils.resource.Resource
import com.app.movieexplorer2.utils.resource.ResponseHandler
import com.app.xoco2.data.models.RegisterRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class AuthRepository(
    private val api: AuthApiService,
    private val responseHandler: ResponseHandler
) {

    fun signUp(fullName: String, email: String, pass: String): Flow<Resource<RegisterResponse>> = flow {
        emit(Resource.loading())
        try {
            val response = responseHandler.handleResponse(api.userRegistration(
                registerRequest = RegisterRequest(
                    fullName = fullName,
                    email = email,
                    password = pass
                )
            ))
            emit(response)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(responseHandler.handleException(e))
        }
    }.catch {
        it.printStackTrace()
        emit(responseHandler.handleException(it))
    }

    fun logIn(email: String, pass: String): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.loading())
        try {
            val response = responseHandler.handleResponse(api.userLogin(loginRequest = LoginRequest(
                email,
                pass
            )))
            emit(response)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(responseHandler.handleException(e))
        }
    }.catch {
        it.printStackTrace()
        emit(responseHandler.handleException(it))
    }

    fun VerifyEmail(email: String, code: String): Flow<Resource<OtpVerifyResponse>> = flow {
        emit(Resource.loading())
        try {
            val response = responseHandler.handleResponse(api.userVerification(
                verifyRequest = OtpVerifyRequest(
                    email,
                    code
                )
            ))
            emit(response)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(responseHandler.handleException(e))
        }
    }.catch {
        it.printStackTrace()
        emit(responseHandler.handleException(it))
    }
}