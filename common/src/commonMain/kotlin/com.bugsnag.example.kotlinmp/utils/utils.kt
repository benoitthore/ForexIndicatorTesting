package com.bugsnag.example.kotlinmp.utils

import com.bugsnag.example.kotlinmp.log

fun <T> List<T>.last(n: Int) = get(size - n - 1)

fun throwException(message : String) : Nothing {
    throw Exception(message).also { log(it) }
}