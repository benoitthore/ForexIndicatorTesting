package com.bugsnag.example.kotlinmp.lib.wrapper

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.IndicatorData
import com.bugsnag.example.kotlinmp.lib.wrapper.requests.MT4Request
import com.bugsnag.example.kotlinmp.log

interface MT4DataGatherer {
    val ea: EA
    val indicators: List<Indicator> get() = ea.indicators
    fun onStart(startData: StartData)
    fun onNewBar(): List<MT4Request.DataRequest<*>>
    fun responseCallback(map: Map<MT4Request.DataRequest<*>, Iterable<Double>>): List<MT4Request.PositionAction>
    fun actionCallback(success: Boolean)
}


class EAWrapper(
        override val ea: EA
) : MT4DataGatherer {

    private val indicatorsHistory = mutableMapOf<Indicator, MutableList<IndicatorData>>()
    private val closePrices = mutableListOf<Double>()
    private val equity = mutableListOf<Double>()

    override fun onStart(startData: StartData) {
        ea.onStart(startData)
    }

    override fun onNewBar(): List<MT4Request.DataRequest<*>> =
            indicators.map { MT4Request.DataRequest.GetIndicatorValue(it) } +
                    MT4Request.DataRequest.GetClosePrice + MT4Request.DataRequest.GetEquity

    @Suppress("IMPLICIT_CAST_TO_ANY")
    override fun responseCallback(map: Map<MT4Request.DataRequest<*>, Iterable<Double>>): List<MT4Request.PositionAction> {

        map.forEach { (request, response) ->
            when (request) {
                is MT4Request.DataRequest.GetIndicatorValue -> {
                    indicatorsHistory
                            .getOrPut(request.indicator) { mutableListOf() }
                            .add(request.buildFromResponse(response))
                }
                is MT4Request.DataRequest.GetIndicatorNumberOfParams -> {
                    log("CRASH - GetIndicatorNumberOfParams not implemented")
                    TODO()
                }
                is MT4Request.DataRequest.GetClosePrice -> {
                    closePrices += request.buildFromResponse(response)
                }
                is MT4Request.DataRequest.GetEquity -> {
                    equity += request.buildFromResponse(response)
                }
            }.run { }
        }

        return ea.onDataReceived(EAData(
                equity = equity, closePrices = closePrices, indicatorsHistory = indicatorsHistory
        ))
    }


    override fun actionCallback(success: Boolean) {
        log("actionCallback($success)")
    }
}