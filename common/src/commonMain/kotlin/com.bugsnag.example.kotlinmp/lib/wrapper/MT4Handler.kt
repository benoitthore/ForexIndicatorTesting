package com.bugsnag.example.kotlinmp.lib.wrapper

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.Position

interface MT4Handler {
    fun onNewBar(): List<MT4Request.DataRequest<*>>
    fun responseCallback(map: Map<MT4Request.DataRequest<*>, Iterable<Double>>): MT4Request.PositionAction?
    fun actionCallback(success: Boolean)
}

class MT4HandlerImpl : MT4Handler {
    private val indicatorHistory = mutableMapOf<Indicator, MutableList<Double>>()

    override fun onNewBar(): List<MT4Request.DataRequest<*>> = listOf(
            MT4Request.DataRequest.GetIndicatorValue(Indicator.ATR),
            MT4Request.DataRequest.GetIndicatorValue(Indicator.MA20)
    )

    override fun responseCallback(map: Map<MT4Request.DataRequest<*>, Iterable<Double>>): MT4Request.PositionAction? {
        map.forEach { (request, response) ->
            when (request) {
                is MT4Request.DataRequest.GetIndicatorValue -> {
                    indicatorHistory
                            .getOrPut(request.indicator) { mutableListOf() }
                            .add(request.buildFromResponse(response))
                }
                is MT4Request.DataRequest.GetIndicatorNumberOfParams -> TODO()
            }.hashCode()
        }

        return getPosition(indicatorHistory)
    }

    fun getPosition(indicators: Map<Indicator, List<Double>>): MT4Request.PositionAction? {

        val atrValue = (indicators[Indicator.ATR] ?: error("No ATR value")).last()
        val ma20Value = (indicators[Indicator.MA20] ?: error("No MA20 value")).last()

        return MT4Request.PositionAction.OpenPosition(
                Position(Position.Type.LONG, 123, atrValue, ma20Value)
        )
    }

    override fun actionCallback(success: Boolean) {
        println("actionCallback($success)")
    }
}