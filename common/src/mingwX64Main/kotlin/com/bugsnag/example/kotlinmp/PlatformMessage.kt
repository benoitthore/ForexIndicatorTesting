package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.utils.throwException
import platform.posix.fclose
import platform.posix.fflush
import platform.posix.fopen
import platform.posix.fprintf

actual fun platformMessage() = "Hello, Kotlin Windows"
actual fun platformId(): Int = 42


private class Logger(val logFile: String) {
    private val logHandle by lazy {
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

private val root = "C:\\Users\\Bebeuz\\AppData\\Roaming\\MetaQuotes\\Terminal\\73B7A2420D6397DFF9014A20F1201F97\\MQL5\\Libraries\\"
private val commonErrorsFile = root + "errors.txt"

actual object Log : ILog {
    override var index: Int? = 0

    private fun indexOrThrow() = index ?: throwException("Log index not set") { message ->
        val log = Logger(commonErrorsFile)
        log(message)
        log.close()
    }

    private val defaultLog by lazy { Logger(root + "${indexOrThrow()}_kotlin.log") }
    private val inputLog by lazy { Logger(root + "${indexOrThrow()}_kotlin_io.log") }


    override fun d(message: Any?) {
        defaultLog(message)
    }

    override fun e(message: Any?) {
        defaultLog("ERROR: $message")
        close()
    }

    override fun io(message: Any?) {
        inputLog(message)
    }

    fun close() {
        inputLog.close()
        defaultLog.close()
    }
}