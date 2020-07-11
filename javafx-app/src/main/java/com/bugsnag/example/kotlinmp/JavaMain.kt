package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.Indicator.*
import com.bugsnag.example.kotlinmp.lib.Position
import com.bugsnag.example.kotlinmp.lib.wrapper.*
import java.lang.IllegalArgumentException


fun main() {
    val wrapper = MT4ServiceImpl(MT4DataGathererImpl(ATR, MA20) { closePrices, indicators ->
        val atrValue = (indicators[ATR] ?: error("No ATR value")).last()
        val ma20Value = (indicators[MA20] ?: error("No MA20 value")).last()

        listOf(
                MT4Request.PositionAction.OpenPosition(
                        Position(Position.Type.LONG, 123, ma20Value.value1, ma20Value.value7, takeProfit = closePrices.last())
                )
        )
    })
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

