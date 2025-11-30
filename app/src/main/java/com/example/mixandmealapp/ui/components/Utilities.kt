package com.example.mixandmealapp.ui.components


fun String.truncate(maxLength: Int): String {
    return if (this.length > maxLength) {
        this.take(maxLength) + "..."
    } else {
        this
    }
}