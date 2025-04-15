package com.example.webtochromedemo.utility

import android.net.Uri
import androidx.core.net.toUri
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration

fun getAuthorizationServiceConfiguration(): AuthorizationServiceConfiguration {
    return AuthorizationServiceConfiguration(
        Uri.parse(AuthConfig.AUTH_URI),
        Uri.parse(AuthConfig.TOKEN_URI),
        null, // registration endpoint
        Uri.parse(AuthConfig.END_SESSION_URI)
    )
}

fun getAuthRequest(): AuthorizationRequest {
    return AuthorizationRequest.Builder(
        getAuthorizationServiceConfiguration(),
        AuthConfig.CLIENT_ID,
        AuthConfig.RESPONSE_TYPE,
        AuthConfig.CALLBACK_URL.toUri()
    ).setScope(AuthConfig.SCOPE).build()
}