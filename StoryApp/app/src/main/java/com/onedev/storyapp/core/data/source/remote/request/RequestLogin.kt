package com.onedev.storyapp.core.data.source.remote.request

data class RequestLogin(
    val email: String,
    val password: String
) {
    companion object {
        fun setRequestLogin(email: String, password: String): RequestLogin {
            return RequestLogin(email, password)
        }
    }
}