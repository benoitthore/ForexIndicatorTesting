package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.Indicator.*
import com.bugsnag.example.kotlinmp.lib.IndicatorData
import com.bugsnag.example.kotlinmp.lib.Position
import com.bugsnag.example.kotlinmp.lib.Symbol
import com.bugsnag.example.kotlinmp.lib.wrapper.*
import com.bugsnag.example.kotlinmp.lib.wrapper.requests.MT4Request

// This should trigger a long signal if MA goes above price
fun main() {
    val ea = getTestEA()
    ea.onStart(StartData(Symbol.EURUSD, 10.0))
    val indicatorsHistory = mutableMapOf<Indicator, MutableList<IndicatorData>>()

    val atrList = mutableListOf<IndicatorData>()
    indicatorsHistory[Indicator.ATR] = atrList

    val maList = mutableListOf<IndicatorData>()
    indicatorsHistory[Indicator.MA] = maList

    val priceList = mutableListOf<Double>()

    val equity = listOf(1000.0);

    fun add(ma: Number,price:Number) {
        priceList += price.toDouble()
        atrList += IndicatorData(14.0)
        maList += IndicatorData(ma.toDouble())
        println(ea.onDataReceived(EAData(equity, priceList, indicatorsHistory)).map { it.position })
        println()
    }


    add(10,20)
    add(30,20)
    add(10,20)
}

//fun main() {
//
//    getPosition(
//            currentPrice = 0.0,
//            pipSize = 0.0,
//            type = Position.Type.LONG,
//            magicNumber = 123,
//            atr = 10.0,
//            equity = 100.0,
//            setTP = true
//    )
//
//
//    val ea = EAWrapper({}, ATR, MA) { //
//
//        val atrValue = it.indicatorsHistory[ATR]!!.last()
//
//        listOf(
//                MT4Request.PositionAction.OpenPosition(
//                        Position(Position.Type.LONG, 12, 12.0, 12.0, 12.0)
//                )
//        )
//    }
//
//    val wrapper = MT4ServiceImpl(ea)
//
//    val mockMT4 = MockMT4(wrapper)
//
//    mockMT4.onStart()
//    mockMT4.onTick()
//    println()
//    mockMT4.onTick()
//    println()
//    mockMT4.onTick()
//}
