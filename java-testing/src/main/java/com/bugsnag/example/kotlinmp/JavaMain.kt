package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.Indicator.*
import com.bugsnag.example.kotlinmp.lib.IndicatorBehaviour
import com.bugsnag.example.kotlinmp.lib.IndicatorData
import com.bugsnag.example.kotlinmp.lib.Position
import com.bugsnag.example.kotlinmp.lib.wrapper.*
import com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange.MT4ClientImpl
import com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange.requests.MT4Request

// This should trigger a long signal if MA goes above price
//fun main() {
//    val ea = VPEA()
//    ea.onStart(StartData(Symbol.EURUSD, 10.0))
//    val indicatorsHistory = mutableMapOf<Indicator, MutableList<IndicatorData>>()
//
//    val atrList = mutableListOf<IndicatorData>()
//    indicatorsHistory[Indicator.ATR] = atrList
//
//    val maList = mutableListOf<IndicatorData>()
//    indicatorsHistory[Indicator.MA] = maList
//
//    val priceList = mutableListOf<Double>()
//
//    val equity = listOf(1000.0);
//
//    fun add(ma: Number,price:Number) {
//        priceList += price.toDouble()
//        atrList += IndicatorData(14.0)
//        maList += IndicatorData(ma.toDouble())
//        println(ea.onDataReceived(EAData(equity, priceList, indicatorsHistory)).map { it.position })
//        println()
//    }
//
//
//    add(10,20)
//    add(30,20)
//    add(10,20)
//}
operator fun <T> List<T>.get(vararg indices : Int) : List<T> = indices.toList().map { get(it) }

fun main() {


    println(Strategy.Builder().configList[1110,683,606])
    println(Strategy.Builder().configList.size)
    return

    Strategy.Builder().configList.forEachIndexed { index, eaConfig ->
        if (eaConfig.entry.indicator == AROON_UP_DOWN) {
            println("$index -> ${eaConfig.entry}")
        }
    }

    return
    val ea = object : EA {
        override val indicators: List<Indicator>
            get() = listOf(ATR, ASCTREND_INDICATOR)

        override fun onDataReceived(data: EAData): List<MT4Request.PositionAction> {
            data.equity.first()
            return listOf(
                    MT4Request.PositionAction.OpenPosition(
                            Position(Position.Type.LONG, 12, 12.0, 12.0, 12.0)
                    )
            )
        }

        override fun onStart(data: StartData) {
//            TODO("Not yet implemented")
        }

    }
    val wrapper = MT4ClientImpl(MT4HandlerImpl(ea))

    val mockMT4 = MockMT4(wrapper)

    mockMT4.onStart()
    mockMT4.onTick()
    println()
    mockMT4.onTick()
    println()
    mockMT4.onTick()
}
