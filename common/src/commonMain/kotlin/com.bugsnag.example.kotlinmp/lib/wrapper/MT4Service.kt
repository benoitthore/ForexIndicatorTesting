package com.bugsnag.example.kotlinmp.lib.wrapper

import com.bugsnag.example.kotlinmp.lib.Symbol
import com.bugsnag.example.kotlinmp.lib.wrapper.requests.MT4Request
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer

interface MT4Service {

    fun onStart(symbol: Int, pipPrice: Double)

    // Notify of new bar to prepare request
    fun onNewBar()

    // execute request
    fun request(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>): Boolean

    // get responses
    fun response(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>)
    fun goToActionMode()

}

class MT4ServiceImpl(
        private val handler: MT4DataGatherer
) : MT4Service {

    private val requestExchangeManager = RequestExchangeManager<MT4Request.DataRequest<*>>()
    private val actionExchangeManager = ActionExchangeManager(handler)
    private var currentManager: MT4ExchangeManager<*> = requestExchangeManager

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

class RequestExchangeManager<T : MT4Request.DataRequest<*>> : MT4ExchangeManager<T>() {
    override fun response(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>): Boolean {
        if (requests.isNotEmpty()) {
            requests.removeAt(0).let { action ->
                val copy = arrayPointer.copy()
                responses[action] = copy
            }
        }

        return requests.isNotEmpty()
    }

}

class ActionExchangeManager(private val handler: MT4DataGatherer) : MT4ExchangeManager<MT4Request.PositionAction>() {

    override fun response(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>): Boolean {
        if (requests.isNotEmpty()) {
            requests.removeAt(0).let { action ->
                handler.actionCallback(
                        action.buildFromResponse(arrayPointer)
                )
            }
        }

        return requests.isNotEmpty()
    }
}
