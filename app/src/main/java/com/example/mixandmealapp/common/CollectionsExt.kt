package com.example.mixandmealapp.common

/**
 * Small shared collection helpers to avoid duplicating logic across ViewModels.
 */
fun <T> Set<T>.toggle(item: T): Set<T> =
    if (contains(item)) this - item else this + item
