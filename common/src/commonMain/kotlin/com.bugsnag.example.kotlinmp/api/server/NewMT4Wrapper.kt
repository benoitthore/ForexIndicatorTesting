package com.bugsnag.example.kotlinmp.api.server

import com.bugsnag.example.kotlinmp.api.server.action.MT4Request
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer


/*
    onTick {
        request(action) { response ->

        }
    }
    This can easily become a suspend function
 */

interface NewMT4Wrapper {

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

interface NewMT4API {
    val onNewBar: () -> List<MT4Request<*>>
    val responseCallback: (Map<MT4Request<*>, Iterable<Double>>) -> MT4Request.PositionAction
    val actionCallback: (Boolean) -> Unit
}

class NewMT4WrapperImpl(
        private val onNewBar: () -> List<MT4Request<*>>,
        private val responseCallback: (Map<MT4Request<*>, Iterable<Double>>) -> MT4Request.PositionAction,
        private val actionCallback: (Boolean) -> Unit
) : NewMT4Wrapper {

    private val requests: MutableList<MT4Request<*>> = mutableListOf()
    private val responses: MutableMap<MT4Request<*>, Iterable<Double>> = mutableMapOf()
    private var action: MT4Request.PositionAction? = null

    override fun onNewBar() {
        onNewBar.invoke()
    }


    override fun request(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>): Boolean {
        if (requests.isEmpty()) return false

        val action = requests.first()
        action.buildRequest(actionPointer, arrayPointer)
        return true
    }

    override fun response(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        requests.removeAt(0).let { action ->
            responses[action] = arrayPointer
        }

        if (requests.isEmpty()) {
            responseCallback(responses)
        }
    }

    override fun action(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        action?.buildRequest(actionPointer, arrayPointer)
    }

    override fun actionResponse(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        action
                ?.buildFromResponse(arrayPointer)
                ?.let { actionResult -> actionCallback(actionResult) }
    }
}

