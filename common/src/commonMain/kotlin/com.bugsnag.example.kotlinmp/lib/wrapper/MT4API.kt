package com.bugsnag.example.kotlinmp.lib.wrapper

import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer


/*
    onTick {
        request(action) { response ->

        }
    }
    This can easily become a suspend function
 */

interface MT4API {

    // Notify of new bar to prepare request
    fun onNewBar()

    // execute request
    fun request(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>): Boolean

    // get responses
    fun response(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>)

    // When all requests done, execute action
    fun action(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>)

    // When action request done, get its result
    fun actionResponse(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>)
}

class MT4APIImpl(
        private val handler: MT4Handler
) : MT4API {

    private var requests: MutableList<MT4Request.DataRequest<*>> = mutableListOf()
    private val responses: MutableMap<MT4Request.DataRequest<*>, Iterable<Double>> = mutableMapOf()
    private var action: MT4Request.PositionAction? = null

    override fun onNewBar() {
        requests = handler.onNewBar().toMutableList()
    }


    override fun request(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>): Boolean {
        if (requests.isEmpty()) return false

        val action = requests.first()
        action.buildRequest(actionPointer, arrayPointer)
        return true
    }

    override fun response(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {

        if (requests.isNotEmpty()) {
            requests.removeAt(0).let { action ->
                responses[action] = arrayPointer.copy()
            }
        }

        if (requests.isEmpty()) {
            action = handler.responseCallback(responses)
        }
    }

    override fun action(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        action?.buildRequest(actionPointer, arrayPointer)
    }

    override fun actionResponse(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        action
                ?.buildFromResponse(arrayPointer)
                ?.let { actionResult -> handler.actionCallback(actionResult) }
    }
}

