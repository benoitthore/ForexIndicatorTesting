package com.bugsnag.example.kotlinmp.utils

import com.bugsnag.example.kotlinmp.Log

annotation class VisibleForTesting

fun <T> List<T>.last(n: Int) = get(size - n - 1)

fun throwException(message : String) : Nothing {
    Log.e(message)
    throw Exception(message)
}

