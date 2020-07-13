package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.Indicator.*
import com.bugsnag.example.kotlinmp.lib.Position
import com.bugsnag.example.kotlinmp.lib.wrapper.*
import com.bugsnag.example.kotlinmp.lib.wrapper.requests.MT4Request


fun main() {

    getPosition(
            currentPrice = 0.0,
            pipsToPrice = 0.0,
            type = Position.Type.LONG,
            magicNumber = 123,
            atr = 10.0,
            equity = 100.0,
            setTP = true
    )


    val ea = EAWrapper({}, ATR, MA) { //

        val atrValue = it.indicatorsHistory[ATR]!!.last()

        listOf(
                MT4Request.PositionAction.OpenPosition(
                        Position(Position.Type.LONG, 12, 12.0, 12.0, 12.0)
                )
        )
    }

    val wrapper = MT4ServiceImpl(ea)

    val mockMT4 = MockMT4(wrapper)

    mockMT4.onStart()
    mockMT4.onTick()
    println()
    mockMT4.onTick()
    println()
    mockMT4.onTick()
}
