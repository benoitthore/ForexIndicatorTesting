package com.bugsnag.example.kotlinmp.utils

import com.bugsnag.example.kotlinmp.ILog
import com.bugsnag.example.kotlinmp.Log
import kotlin.math.pow

annotation class VisibleForTesting

fun <T> List<T>.last(n: Int) = get(size - n - 1)

fun throwException(message: String, logE: ((Any?) -> Unit) = { Log.e(it) }): Nothing {
    logE(message)
    throw Exception(message)
}

fun throwException(exception: Throwable, logE: ((Any?) -> Unit) = { Log.e(it) }): Nothing {
    logE(exception)
    throw exception
}

fun <T> Result<T>.getOrLogException(): T {
    exceptionOrNull()?.let { throwException(it) }
    return getOrNull()!!
}


fun Double.roundToDecimal(decimal : Int) : Double{
    val n = 10.toDouble().pow(decimal).toInt()

    return (this * n).toInt().toDouble() / n
}