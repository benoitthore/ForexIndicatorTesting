package com.bugsnag.example.kotlinmp

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.native.*
import com.soywiz.*;
import kotlin.native.concurrent.Worker

actual fun platformMessage() = "Hello, Kotlin Windows"
actual fun platformId(): Int = 42

actual fun <T> myRunBlocking(block: suspend () -> T): T {
    TODO()
}
