package com.example.webtochromedemo

import com.example.webtochromedemo.utility.AuthConfig
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.TokenRequest
import kotlin.coroutines.suspendCoroutine

class AuthRepository {

    suspend fun performTokenRequest(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ): TokensModel {
        return performTokenRequestSuspend(authService, tokenRequest)
    }

    private suspend fun performTokenRequestSuspend(
        authService: AuthorizationService,
        tokenRequest: TokenRequest,
    ): TokensModel {
        return suspendCoroutine { continuation ->
            authService.performTokenRequest(tokenRequest, getClientAuthentication()) { response, ex ->
                when {
                    response != null -> {
                        val tokens = TokensModel(
                            accessToken = response.accessToken.orEmpty(),
                            refreshToken = response.refreshToken.orEmpty(),
                            idToken = response.idToken.orEmpty()
                        )
                        continuation.resumeWith(Result.success(tokens))
                    }
                    ex != null -> { continuation.resumeWith(Result.failure(ex)) }
                    else -> error("unreachable")
                }
            }
        }
    }
}

private fun getClientAuthentication(): ClientAuthentication {
    return ClientSecretPost(AuthConfig.CLIENT_SECRET)
}

data class TokensModel(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String
)