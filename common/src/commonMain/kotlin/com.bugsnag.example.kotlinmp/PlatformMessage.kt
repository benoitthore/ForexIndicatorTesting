package com.bugsnag.example.kotlinmp

expect fun platformMessage(): String
expect fun platformId(): Int
expect fun log(message : Any)