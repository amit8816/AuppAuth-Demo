package com.example.webtochromedemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

class AuthViewModel(application: Application): AndroidViewModel(application) {
    private val authRepository = AuthRepository()
    private val authService: AuthorizationService = AuthorizationService(getApplication<Application>().applicationContext)

    private val loadingMutableStateFlow = MutableStateFlow(false)
    val token = MutableStateFlow<String?>(null)

    fun onAuthCodeReceived(tokenRequest: TokenRequest) {
        viewModelScope.launch {
            runCatching {
                authRepository.performTokenRequest(
                    authService = authService,
                    tokenRequest = tokenRequest
                )
            }.onSuccess {
                token.value = it.accessToken
            }.onFailure {
                token.value = it.message
            }
        }
    }
}