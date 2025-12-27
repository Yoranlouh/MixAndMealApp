package com.example.mixandmealapp.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mixandmealapp.data.SessionRepository
import com.example.mixandmealapp.network.ApiService
import requests.Login
import responses.AuthResponse

// example of how to use the ApiClient and ApiService
class UserRepository() {
    suspend fun login(email: String, password: String): AuthResponse {
        return ApiService.postLogin(Login(email, password))
    }
}