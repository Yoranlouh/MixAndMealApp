package com.example.mixandmealapp.repository

import com.example.mixandmealapp.network.ApiService
import responses.AuthResponse
import requests.Login

// example of how to use the ApiClient and ApiService
class UserRepository {
    suspend fun testLogin(email: String, password: String): AuthResponse? {
class UserRepository() {
    suspend fun login(email: String, password: String): AuthResponse {
        return ApiService.postLogin(Login(email, password))
    }
}


