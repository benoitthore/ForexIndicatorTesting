package com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange

import com.bugsnag.example.kotlinmp.lib.wrapper.MT4Handler
import com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange.requests.MT4Request
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer

class ActionExchangeManager(private val handler: MT4Handler) : MT4ExchangeManager<MT4Request.PositionAction>() {

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