package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.wrapper.EA
import com.bugsnag.example.kotlinmp.lib.wrapper.MT4HandlerImpl
import com.bugsnag.example.kotlinmp.lib.wrapper.MT4Client
import com.bugsnag.example.kotlinmp.lib.wrapper.MT4ClientImpl


object Strategy {
    fun get(ea : EA): MT4Client = MT4ClientImpl(MT4HandlerImpl(ea))
}