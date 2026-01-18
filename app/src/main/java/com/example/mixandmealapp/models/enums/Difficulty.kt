package com.example.mixandmealapp.models.enums

@kotlinx.serialization.Serializable
enum class Difficulty(val difficultyName: String) {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard"),
}