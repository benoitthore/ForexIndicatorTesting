@file:JvmName("SomethingUnique")

package com.bugsnag.example.kotlinmp

import kotlinx.coroutines.runBlocking

actual fun platformMessage() = "Hello, Kotlin JVM"
actual fun platformId(): Int = 43
actual fun <T> myRunBlocking(block: suspend () -> T): T  = runBlocking { block() }