package com.app.movieexplorer2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.movieexplorer2.data.repo.AuthRepository
import com.app.movieexplorer2.ui.uiState.LoginUiState
import com.app.movieexplorer2.ui.uiState.MovieUiState
import com.app.movieexplorer2.ui.uiState.RegisterUiState
import com.app.movieexplorer2.ui.uiState.VerifyUiState
import com.app.movieexplorer2.utils.resource.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.xml.namespace.QName

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _registerState =
        MutableStateFlow(RegisterUiState())

    val registerState: StateFlow<RegisterUiState> =
            _registerState.asStateFlow()

    fun resetRegisterState() {
        _loginState.value = LoginUiState()
    }

    private val _loginState =
        MutableStateFlow(LoginUiState())

    val loginState: StateFlow<LoginUiState> =
        _loginState.asStateFlow()

    fun resetLoginState() {
        _loginState.value = LoginUiState()
    }

    private val _verifyState =
        MutableStateFlow(VerifyUiState())

    val verifyState: StateFlow<VerifyUiState> =
        _verifyState.asStateFlow()

    fun resetVerifyState() {
        _loginState.value = LoginUiState()
    }

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ) {
        repository.signUp(
            fullName,
            email,
            password
        ).onEach { result ->
            when(result.status) {
                Status.LOADING -> {
                    _registerState.value =
                        RegisterUiState(
                            isLoading = true
                        )
                }

                Status.SUCCESS -> {
                    _registerState.value =
                        RegisterUiState(
                            success = true,
                            isLoading = false
                        )
                }

                Status.ERROR -> {
                    _registerState.value =
                        RegisterUiState(
                            error = result.error,
                            isLoading = false
                        )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun LoginUser(
        email: String,
        password: String
    ) {
        repository.logIn(
            email,
            password
        ).onEach { result ->
            when(result.status) {
                Status.LOADING -> {
                    _loginState.value =
                        LoginUiState(
                            isLoading = true
                        )
                }

                Status.SUCCESS -> {
                    val loginResponse = result.data

                    if (loginResponse != null &&
                        loginResponse.access.isNotBlank()
                    ) {
                        _loginState.value = LoginUiState(
                            isLoading = false,
                            success = true,
                            data = loginResponse
                        )
                    } else {
                        _loginState.value = LoginUiState(
                            isLoading = false,
                            error = result.error
                        )
                    }
                }

                Status.ERROR -> {
                    _loginState.value =
                        LoginUiState(
                            error = result.error,
                            isLoading = false
                        )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun VerifyUser(
        email: String,
        code: String
    ) {
        repository.VerifyEmail(
            email,
            code
        ).onEach { result ->
            when(result.status) {
                Status.LOADING -> {
                    _verifyState.value =
                        VerifyUiState(
                            isLoading = true
                        )
                }

                Status.SUCCESS -> {
                   val verifyResponse = result.data

                   if(verifyResponse != null &&
                       verifyResponse.access.isNotBlank()) {
                       _verifyState.value = VerifyUiState(
                           isLoading = false,
                           success = true,
                           data = verifyResponse
                       )
                   } else {
                       _verifyState.value = VerifyUiState(
                           isLoading = false,
                           error = result.error
                       )
                   }

                }

                Status.ERROR -> {
                    _verifyState.value =
                        VerifyUiState(
                            error = result.error,
                            isLoading = false
                        )
                }
            }
        }.launchIn(viewModelScope)
    }
}