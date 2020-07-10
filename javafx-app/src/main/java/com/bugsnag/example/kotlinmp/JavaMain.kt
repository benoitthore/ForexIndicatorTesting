package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.api.client.Indicator
import com.bugsnag.example.kotlinmp.api.client.MT4API
import com.bugsnag.example.kotlinmp.api.client.Position


fun main() {
    test(MocKApi())
}

class MocKApi : MT4API {
    override fun close() {
        TODO("Not yet implemented")
    }

    override fun getClosePrice(): Double = 42.0

    override fun getIndicatorValue(indicator: Indicator): Double {
        TODO("Not yet implemented")
    }

    override fun getIndicatorNumberOfParams(indicator: Indicator): Int {
        TODO("Not yet implemented")
    }

    override fun openPosition(position: Position): Boolean {
        TODO("Not yet implemented")
    }

    override fun closePosition(position: Position): Boolean {
        TODO("Not yet implemented")
    }

    override fun moveStopLoss(toValue: Double): Boolean {
        TODO("Not yet implemented")
    }

}