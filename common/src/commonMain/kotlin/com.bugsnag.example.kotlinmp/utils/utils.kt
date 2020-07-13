package com.bugsnag.example.kotlinmp.utils

fun <T> List<T>.last(n: Int) = get(size - n - 1)

fun throwException(message : String) : Nothing {
    // TODO Replace with writeToFile
    throw Exception(message)
}