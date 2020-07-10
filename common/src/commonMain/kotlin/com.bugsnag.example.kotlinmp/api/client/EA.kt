package com.bugsnag.example.kotlinmp.api.client

interface EA {
    val api: MT4API
    fun onStart(): Boolean
    fun onTick()
}