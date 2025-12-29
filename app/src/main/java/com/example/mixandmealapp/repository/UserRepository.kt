package com.example.mixandmealapp.repository

import android.util.Log
import com.example.mixandmealapp.models.entries.TokenClaim
import com.example.mixandmealapp.models.enums.Role
import com.example.mixandmealapp.models.requests.Login
import com.example.mixandmealapp.models.responses.AuthResponse
import com.example.mixandmealapp.models.responses.RoleResponse
import com.example.mixandmealapp.network.ApiService


// example of how to use the ApiClient and ApiService
class UserRepository() {
    suspend fun login(email: String, password: String): AuthResponse {
        return ApiService.postLogin(Login(email, password))
    }

    suspend fun checkRole(token: String): RoleResponse? {
        val response = ApiService.checkRole(token)
        Log.d("UserRepo", "📡 RoleResponse: $response")
        return response
    }
}
