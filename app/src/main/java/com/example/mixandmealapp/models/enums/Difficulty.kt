package com.example.mixandmealapp.models.enums

@kotlinx.serialization.Serializable
enum class Difficulty(difficultyName: String) {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard"),
}