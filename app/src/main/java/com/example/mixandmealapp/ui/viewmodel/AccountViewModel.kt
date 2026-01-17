package com.example.mixandmealapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mixandmealapp.data.TokenRepository
import com.example.mixandmealapp.models.entries.AllergenEntry
import com.example.mixandmealapp.models.entries.DietEntry
import com.example.mixandmealapp.models.requests.AllergenIDRequest
import com.example.mixandmealapp.models.requests.DietsIDRequest
import com.example.mixandmealapp.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AccountUiState(
    val name: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val isSaving: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false,
    val userAllergens: List<AllergenEntry> = emptyList(),
    val userDiets: List<DietEntry> = emptyList(),
    val allAvailableAllergens: List<AllergenEntry> = emptyList(),
    val allAvailableDiets: List<DietEntry> = emptyList()
)

class AccountViewModel(
    private val userRepo: UserRepository,
    private val tokenRepo: TokenRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState = _uiState.asStateFlow()

    fun load() {
        _uiState.value = _uiState.value.copy(error = null)
        viewModelScope.launch {
            try {
                val token = tokenRepo.getTokenOrDefault()
                // In een echte app zouden we ook het profiel laden, maar de focus ligt nu op allergenen en diëten.
                val allergens = userRepo.getAllergens(token)
                val diets = userRepo.getDiets(token)
                val allAllergens = userRepo.getAllAllergens()
                val allDiets = userRepo.getAllDiets()

                android.util.Log.d("AccountVM", "Loaded: userAllergens=${allergens.size}, allAvailableAllergens=${allAllergens.size}, userDiets=${diets.size}, allAvailableDiets=${allDiets.size}")

                _uiState.value = _uiState.value.copy(
                    userAllergens = allergens,
                    userDiets = diets,
                    allAvailableAllergens = allAllergens,
                    allAvailableDiets = allDiets,
                    error = null
                )
            } catch (t: Throwable) {
                android.util.Log.e("AccountVM", "Error loading data", t)
                _uiState.value = _uiState.value.copy(error = t.message)
            }
        }
    }

    fun addAllergen(allergen: AllergenIDRequest) {
        _uiState.value = _uiState.value.copy(isSaving = true)
        viewModelScope.launch {
            try {
                val token = tokenRepo.getTokenOrDefault()
                val updated = userRepo.addAllergen(token, allergen)
                _uiState.value = _uiState.value.copy(userAllergens = updated, isSaving = false, error = null)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message, isSaving = false)
            }
        }
    }

    fun removeAllergen(allergen: AllergenIDRequest) {
        _uiState.value = _uiState.value.copy(isSaving = true)
        viewModelScope.launch {
            try {
                val token = tokenRepo.getTokenOrDefault()
                val updated = userRepo.removeAllergen(token, allergen)
                _uiState.value = _uiState.value.copy(userAllergens = updated, isSaving = false, error = null)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message, isSaving = false)
            }
        }
    }

    fun addDiet(diet: DietsIDRequest) {
        _uiState.value = _uiState.value.copy(isSaving = true)
        viewModelScope.launch {
            try {
                val token = tokenRepo.getTokenOrDefault()
                val updated = userRepo.addDiet(token, diet)
                _uiState.value = _uiState.value.copy(userDiets = updated, isSaving = false, error = null)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message, isSaving = false)
            }
        }
    }

    fun removeDiet(diet: DietsIDRequest) {
        _uiState.value = _uiState.value.copy(isSaving = true)
        viewModelScope.launch {
            try {
                val token = tokenRepo.getTokenOrDefault()
                val updated = userRepo.removeDiet(token, diet)
                _uiState.value = _uiState.value.copy(userDiets = updated, isSaving = false, error = null)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message, isSaving = false)
            }
        }
    }

    fun onNameChange(value: String) { _uiState.value = _uiState.value.copy(name = value, saved = false) }
    fun onEmailChange(value: String) { _uiState.value = _uiState.value.copy(email = value, saved = false) }
    fun setAvatar(url: String?) { _uiState.value = _uiState.value.copy(avatarUrl = url, saved = false) }

    fun save() {
        _uiState.value = _uiState.value.copy(isSaving = true, error = null)
        viewModelScope.launch {
            try {
                // userRepo.saveProfile(UserProfile(name = uiState.value.name, email = uiState.value.email, avatarUrl = uiState.value.avatarUrl))
                _uiState.value = _uiState.value.copy(isSaving = false, saved = true)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(isSaving = false, error = t.message)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                tokenRepo.clearToken()
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t.message)
            }
        }
    }
}
