package com.bugsnag.example.kotlinmp.utils

import com.bugsnag.example.kotlinmp.ILog
import com.bugsnag.example.kotlinmp.Log

annotation class VisibleForTesting

fun <T> List<T>.last(n: Int) = get(size - n - 1)

fun throwException(message: String, logE: ((Any?) -> Unit) = { Log.e(it) }): Nothing {
    logE(message)
    throw Exception(message)
}

