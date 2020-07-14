package com.bugsnag.example.kotlinmp.lib.wrapper

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.IndicatorData
import com.bugsnag.example.kotlinmp.lib.Symbol
import com.bugsnag.example.kotlinmp.lib.wrapper.requests.MT4Request

typealias OnNewBar = (EAData) -> List<MT4Request.PositionAction>
typealias OnStart = (StartData) -> Unit

data class EAData(
        val equity: List<Double>,
        val closePrices: List<Double>,
        val indicatorsHistory: Map<Indicator, MutableList<IndicatorData>>
)

data class StartData(val symbol: Symbol, val pipSize: Double)

interface EA {
    val indicators: List<Indicator>
    val onDataReceived: OnNewBar
    val onStart: OnStart

    companion object {
        fun create(indicators: List<Indicator>, onStart: OnStart, onDataReceived: OnNewBar) = object : EA {
            override val indicators: List<Indicator>
                get() = indicators
            override val onDataReceived: OnNewBar
                get() = onDataReceived
            override val onStart: OnStart
                get() = onStart
        }
    }
}