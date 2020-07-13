package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.Indicator.*
import com.bugsnag.example.kotlinmp.lib.Position
import com.bugsnag.example.kotlinmp.lib.wrapper.*
import com.bugsnag.example.kotlinmp.lib.wrapper.requests.MT4Request


fun main() {
    val ea = EAWrapper(ATR, MovingAverage) { //

        ( _, closePrices, indicators ) ->

        val atrValue = indicators[ATR]!!.last()

        emptyList()
    }

    val wrapper = MT4ServiceImpl(ea)

    val mockMT4 = MockMT4(wrapper)

    mockMT4.onStart()
    mockMT4.onTick()
    println()
    mockMT4.onTick()
    println()
    mockMT4.onTick()
//    println()
//    mockMT4.onTick()
//    println()
//    mockMT4.onTick()
}
