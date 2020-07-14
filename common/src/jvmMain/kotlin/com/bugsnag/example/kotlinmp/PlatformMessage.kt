@file:JvmName("SomethingUnique")

package com.bugsnag.example.kotlinmp


actual fun platformMessage() = "Hello, Kotlin JVM"
actual fun platformId(): Int = 43
actual fun log(message: Any) {
    println(message)
}