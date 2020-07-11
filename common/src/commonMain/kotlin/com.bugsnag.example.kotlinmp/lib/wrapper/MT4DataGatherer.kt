package com.bugsnag.example.kotlinmp.lib.wrapper

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.IndicatorData

interface MT4DataGatherer {
    fun onNewBar(): List<MT4Request.DataRequest<*>>
    fun responseCallback(map: Map<MT4Request.DataRequest<*>, Iterable<Double>>): List<MT4Request.PositionAction>
    fun actionCallback(success: Boolean)
}

typealias ControlPositionCallback = (closePrices: List<Double>, indicators: Map<Indicator, MutableList<IndicatorData>>) -> List<MT4Request.PositionAction>

interface EaApi {
    val indicators: List<Indicator>
    val onDataReceived: ControlPositionCallback
}

class EaApiImpl(
        override val indicators: List<Indicator>,
        override val onDataReceived: ControlPositionCallback
) : MT4DataGatherer, EaApi {
    constructor(vararg indicators: Indicator, onDataReceived: ControlPositionCallback) : this(indicators.toList(), onDataReceived)

    private val indicatorHistory = mutableMapOf<Indicator, MutableList<IndicatorData>>()
    private val closePrices = mutableListOf<Double>()

    override fun onNewBar(): List<MT4Request.DataRequest<*>> =
            indicators.map { MT4Request.DataRequest.GetIndicatorValue(it) } + MT4Request.DataRequest.GetClosePrice


    @Suppress("IMPLICIT_CAST_TO_ANY")
    override fun responseCallback(map: Map<MT4Request.DataRequest<*>, Iterable<Double>>): List<MT4Request.PositionAction> {

        map.forEach { (request, response) ->
            when (request) {
                is MT4Request.DataRequest.GetIndicatorValue -> {
                    indicatorHistory
                            .getOrPut(request.indicator) { mutableListOf() }
                            .add(request.buildFromResponse(response))
                }
                is MT4Request.DataRequest.GetIndicatorNumberOfParams -> TODO()
                is MT4Request.DataRequest.GetClosePrice -> {
                    closePrices += request.buildFromResponse(response)
                }
            }.run { }
        }

        return onDataReceived(closePrices, indicatorHistory)
    }


    override fun actionCallback(success: Boolean) {
        println("actionCallback($success)")
    }
}