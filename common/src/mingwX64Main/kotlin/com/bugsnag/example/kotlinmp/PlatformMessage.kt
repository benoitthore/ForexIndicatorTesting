package com.bugsnag.example.kotlinmp

import platform.posix.fclose
import platform.posix.fflush
import platform.posix.fopen
import platform.posix.fprintf

actual fun platformMessage() = "Hello, Kotlin Windows"
actual fun platformId(): Int = 42

private val defaultLog = Logger("C:\\Users\\Bebeuz\\AppData\\Roaming\\MetaQuotes\\Terminal\\73B7A2420D6397DFF9014A20F1201F97\\MQL5\\Libraries\\kotlin.log")
private val inputLog = Logger("C:\\Users\\Bebeuz\\AppData\\Roaming\\MetaQuotes\\Terminal\\73B7A2420D6397DFF9014A20F1201F97\\MQL5\\Libraries\\kotlin_io.log")

private class Logger(val logFile: String) {
    val logHandle by lazy {
        fopen(logFile, "a")
    }

    fun log(message: Any?) {
        val message = message ?: "null"
        fprintf(logHandle, "$message\n")
        fflush(logHandle)
    }

    operator fun invoke(message: Any?) = log(message)

    fun close() {
        fclose(logHandle)
    }
}



actual object Log : ILog {
    override fun d(message: Any?) {
        defaultLog(message)
    }

    override fun e(message: Any?) {
        defaultLog("ERROR: $message")
        close()
    }

    override  fun io(message: Any?) {
        inputLog(message)
    }

    fun close() {
        inputLog.close()
        defaultLog.close()
    }
}