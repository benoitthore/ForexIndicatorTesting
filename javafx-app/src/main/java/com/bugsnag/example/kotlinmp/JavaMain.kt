package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.Indicator.*
import com.bugsnag.example.kotlinmp.lib.Position
import com.bugsnag.example.kotlinmp.lib.wrapper.*
import com.bugsnag.example.kotlinmp.lib.wrapper.requests.MT4Request


fun main() {
    val testEA = EAWrapper(ATR, MovingAverage) { //

        _, closePrices, indicators ->

        val atrValue = (indicators[ATR] ?: error("No ATR value")).last()
        val ma20Value = (indicators[MovingAverage] ?: error("No MA20 value")).last()

        listOf(
                MT4Request.PositionAction.OpenPosition(
                        Position(Position.Type.LONG, 3, ma20Value.value1, ma20Value.value7, takeProfit = closePrices.last())
                )
        )
    }
    val VPEA = EAWrapper(ATR, MovingAverage) { //

        _, closePrices, indicators ->

        val atrValue = indicators[ATR]!!.last()

        emptyList()
    }
    val wrapper = MT4ServiceImpl(VPEA)
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

class VPEA {

}