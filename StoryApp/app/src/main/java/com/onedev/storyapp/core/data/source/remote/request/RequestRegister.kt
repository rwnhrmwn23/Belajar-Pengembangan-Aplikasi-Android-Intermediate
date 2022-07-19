package com.onedev.storyapp.core.data.source.remote.request

data class RequestRegister(
    val name: String,
    val email: String,
    val password: String
) {
    companion object {
        fun setRequestRegister(name: String, email: String, password: String): RequestRegister {
            return RequestRegister(name, email, password)
        }
    }
}