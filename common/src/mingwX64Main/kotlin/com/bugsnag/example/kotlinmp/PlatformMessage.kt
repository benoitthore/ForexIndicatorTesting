package com.bugsnag.example.kotlinmp

import platform.posix.fflush
import platform.posix.fopen
import platform.posix.fprintf

actual fun platformMessage() = "Hello, Kotlin Windows"
actual fun platformId(): Int = 42


val logFile = "C:\\Users\\Bebeuz\\AppData\\Roaming\\MetaQuotes\\Terminal\\73B7A2420D6397DFF9014A20F1201F97\\MQL5\\Libraries\\kotlin.log"
private val logHandle by lazy {
    fopen(logFile, "a")
}
actual fun log(message: Any) {
    fprintf(logHandle, "$message\n")
    fflush(logHandle)
}