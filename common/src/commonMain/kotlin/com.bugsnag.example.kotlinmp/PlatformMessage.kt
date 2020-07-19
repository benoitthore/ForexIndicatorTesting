package com.bugsnag.example.kotlinmp

expect fun platformMessage(): String
expect fun platformId(): Int

interface ILog {
    fun d(message: Any?)
    fun e(message: Any?)
    fun io(message: Any?)
}

expect object Log : ILog