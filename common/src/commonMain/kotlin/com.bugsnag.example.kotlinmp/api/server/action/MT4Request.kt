package com.bugsnag.example.kotlinmp.api.server.action

import com.bugsnag.example.kotlinmp.api.client.Indicators
import com.bugsnag.example.kotlinmp.api.client.Position
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer

sealed class MT4Request<T : Any>(val actionId: Enum<MT4RequestId>) {
    /**
     * Sets the pointer value based on action
     */
    fun buildRequest(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        actionPointer.value = actionId.ordinal
        execute(arrayPointer)
    }

    protected open fun execute(arrayPointer: AbstractedArrayPointer<Double>) {}

    abstract fun buildFromResponse(data: Iterable<Double>): T
    fun buildAndStoreResponse(data: Iterable<Double>) {
        response = buildFromResponse(data)
    }

    var response: T? = null
        private set

    object Close : MT4Request<Unit>(MT4RequestId.Close) {
        override fun buildFromResponse(data: Iterable<Double>) = Unit
    }

    object GetClosePrice : MT4Request<Unit>(MT4RequestId.GetClosePrice) {
        override fun buildFromResponse(data: Iterable<Double>) = Unit
    }

    class GetIndicatorValue(val indicator: Indicators) : MT4Request<Double>(MT4RequestId.GetIndicatorValue) {
        override fun execute(arrayPointer: AbstractedArrayPointer<Double>) {
            arrayPointer[0] = indicator.ordinal.toDouble()
        }

        override fun buildFromResponse(data: Iterable<Double>) = data.first()
    }

    class GetIndicatorNumberOfParams(val indicator: Indicators) : MT4Request<Int>(MT4RequestId.GetIndicatorNumberOfParams) {
        override fun execute(arrayPointer: AbstractedArrayPointer<Double>) {
            arrayPointer[0] = indicator.ordinal.toDouble()
        }

        override fun buildFromResponse(data: Iterable<Double>) = data.first().toInt()
    }

    sealed class PositionAction(val position: Position, actionId: MT4RequestId) : MT4Request<Boolean>(actionId) {
        override fun execute(arrayPointer: AbstractedArrayPointer<Double>) {
            position.apply {
                arrayPointer[0] = type.ordinal.toDouble()
                arrayPointer[1] = magicNumber.toDouble()
                arrayPointer[2] = volume
                arrayPointer[3] = stopLoss
                arrayPointer[4] = takeProfit ?: 0.0
            }
        }

        override fun buildFromResponse(data: Iterable<Double>) = data.first().toInt() == 1

        class OpenPosition(position: Position) : PositionAction(position, MT4RequestId.OpenPosition)
        class UpdatePosition(position: Position) : PositionAction(position, MT4RequestId.UpdatePosition)
        class ClosePosition(position: Position) : PositionAction(position, MT4RequestId.ClosePosition)
    }
}