package com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange

import com.bugsnag.example.kotlinmp.lib.Symbol
import com.bugsnag.example.kotlinmp.lib.wrapper.MT4Handler
import com.bugsnag.example.kotlinmp.lib.wrapper.StartData
import com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange.requests.MT4Request
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer
import com.bugsnag.example.kotlinmp.utils.VisibleForTesting

interface MT4Client {

    fun onStart(symbol: Int, pipPrice: Double)

    // Notify of new bar to prepare request
    fun onNewBar()

    // execute request
    fun request(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>): Boolean

    // get responses
    fun response(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>)
    fun goToActionMode()

}

class MT4ClientImpl(
        private val handler: MT4Handler,
        private val requestExchangeManager: RequestExchangeManager<MT4Request.DataRequest<*>> = RequestExchangeManager(),
        private val actionExchangeManager: ActionExchangeManager = ActionExchangeManager(handler)
) : MT4Client {

    @VisibleForTesting
    var currentManager: MT4ExchangeManager<*> = requestExchangeManager
        private set

    override fun onStart(symbol: Int, pipPrice: Double) {
        handler.onStart(StartData(Symbol.values()[symbol], pipPrice))
    }

    override fun onNewBar() {
        currentManager = requestExchangeManager
        requestExchangeManager.requests = handler.onNewBar().toMutableList()
        requestExchangeManager.responses = mutableMapOf()
    }

    override fun request(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>): Boolean {
        return currentManager.request(actionPointer, arrayPointer)
    }

    override fun response(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        currentManager.response(actionPointer, arrayPointer)
    }

    override fun goToActionMode() {
        val responseCallback = handler.responseCallback(requestExchangeManager.responses)
        actionExchangeManager.requests = responseCallback.toMutableList()

        currentManager = actionExchangeManager
    }

}

abstract class MT4ExchangeManager<T : MT4Request<*>> {
    var requests: MutableList<T> = mutableListOf()
    var responses: MutableMap<T, Iterable<Double>> = mutableMapOf()

    // execute request
    fun request(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>): Boolean {
        if (requests.isEmpty()) return false

        val action = requests.first()
        action.buildRequest(actionPointer, arrayPointer)
        return true
    }

    // get responses
    abstract fun response(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>): Boolean

}

