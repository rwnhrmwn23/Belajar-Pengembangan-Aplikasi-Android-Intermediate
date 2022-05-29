package com.onedev.storyapp.core.data.source.remote.response

object Register {
    data class Request(
        val name: String,
        val email: String,
        val password: String
    )

    data class Response(
        val error: Boolean,
        val message: String
    )
}