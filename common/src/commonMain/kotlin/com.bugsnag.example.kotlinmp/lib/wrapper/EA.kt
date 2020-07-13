package com.bugsnag.example.kotlinmp.lib.wrapper

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.IndicatorData
import com.bugsnag.example.kotlinmp.lib.wrapper.requests.MT4Request

typealias doEAWork = (EAData) -> List<MT4Request.PositionAction>

data class EAData(
        val equity: List<Double>,
        val closePrices: List<Double>,
        val indicatorsHistory: Map<Indicator, MutableList<IndicatorData>>
)

interface EA {
    val indicators: List<Indicator>
    val onDataReceived: doEAWork

    companion object {
        fun create(indicators: List<Indicator>, onDataReceived: doEAWork) = object : EA {
            override val indicators: List<Indicator>
                get() = indicators
            override val onDataReceived: doEAWork
                get() = onDataReceived
        }
    }
}