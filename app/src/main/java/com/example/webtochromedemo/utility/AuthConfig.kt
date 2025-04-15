package com.example.webtochromedemo.utility

import net.openid.appauth.ResponseTypeValues

object AuthConfig {
    const val AUTH_URI = "https://github.com/login/oauth/authorize"
    const val TOKEN_URI = "https://github.com/login/oauth/access_token"
    const val END_SESSION_URI = "https://github.com/logout"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "user,repo"
    const val CLIENT_ID = "Ov23li5ktGQUCiTqnlqD"
    const val CLIENT_SECRET = "270fad57dd101d1ffaa81c776c4b147091193f71"
    const val CALLBACK_URL = "com.example.webtochromedemo://github.com/callback"
    const val LOGOUT_CALLBACK_URL = "com.example.webtochromedemo://github.com/logout_callback"
}