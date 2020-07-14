package com.bugsnag.example.kotlinmp

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.native.*
import kotlin.native.concurrent.Worker

actual fun platformMessage() = "Hello, Kotlin Windows"
actual fun platformId(): Int = 42

