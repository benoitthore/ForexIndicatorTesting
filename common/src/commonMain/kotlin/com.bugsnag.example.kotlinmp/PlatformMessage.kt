package com.bugsnag.example.kotlinmp

expect fun platformMessage(): String

fun test() = platformMessage()