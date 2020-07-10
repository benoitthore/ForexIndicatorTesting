package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.api.client.MT4API

expect fun platformMessage(): String
expect fun platformId(): Int
expect fun <T> myRunBlocking(block: suspend () -> T): T