@file:JvmName("SomethingUnique")

package com.bugsnag.example.kotlinmp


actual fun platformMessage() = "Hello, Kotlin JVM"
actual fun platformId(): Int = 43

actual object Log : ILog {
    override var index: Int? = -1
    set(value) {
        field = value
        println("Index set to $field")
    }

    override fun d(message: Any?) {
        println(message)
    }

    override fun e(message: Any?) {
        println("ERROR: $message")
    }

    override fun io(message: Any?) {
        println("INPUTLOG: $message")
    }
}