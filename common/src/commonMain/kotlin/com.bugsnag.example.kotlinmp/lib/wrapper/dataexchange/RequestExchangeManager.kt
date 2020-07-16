package com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange

import com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange.requests.MT4Request
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer

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