package com.bugsnag.example.kotlinmp.lib.wrapper

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.IndicatorData
import com.bugsnag.example.kotlinmp.lib.Symbol
import com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange.requests.MT4Request

data class EAData(
        val equity: List<Double>,
        val closePrices: List<Double>,
        val indicatorsHistory: Map<Indicator, MutableList<IndicatorData>>
)

data class StartData(val symbol: Symbol, val pipSize: Double)

interface EA {
    val indicators: List<Indicator>
    fun onDataReceived(data: EAData): List<MT4Request.PositionAction>
    fun onStart(data: StartData)
}