package com.example.mixandmealapp.models.requests

@kotlinx.serialization.Serializable
data class Login(val email: String, val password: String)