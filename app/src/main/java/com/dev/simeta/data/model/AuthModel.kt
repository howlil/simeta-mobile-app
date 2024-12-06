package com.dev.simeta.data.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val data: Token?
)

data class Token(
    val token: String
)
