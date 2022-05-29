package com.onedev.storyapp.core.data.source.remote.response

object Login {
    data class Request(
        val email: String,
        val password: String
    )

    data class Response(
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
}