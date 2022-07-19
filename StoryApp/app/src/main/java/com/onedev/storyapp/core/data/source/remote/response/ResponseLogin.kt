package com.onedev.storyapp.core.data.source.remote.response

data class ResponseLogin(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult,
) {
    data class LoginResult(
        val userId: String,
        val name: String,
        val token: String,
    )
}