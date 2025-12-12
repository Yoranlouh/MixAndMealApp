package com.example.mixandmealapp.repository

import com.example.mixandmealapp.network.ApiService
import com.example.mixandmealapp.network.Login
import com.example.mixandmealapp.network.Token

// example of how to use the ApiClient and ApiService
class UserRepository {
    suspend fun login(email: String, password: String): Token {
        return ApiService.postLogin(Login(email, password))
    }
}