package com.bugsnag.example.kotlinmp.lib.wrapper.requests

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.IndicatorData
import com.bugsnag.example.kotlinmp.lib.Position
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

    sealed class DataRequest<T : Any>(actionId: MT4RequestId) : MT4Request<T>(actionId) {

        object GetClosePrice : DataRequest<Double>(MT4RequestId.GetClosePrice) {
            override fun buildFromResponse(data: Iterable<Double>): Double = data.first()
        }

        object GetEquity : DataRequest<Double>(MT4RequestId.GetEquity) {
            override fun buildFromResponse(data: Iterable<Double>): Double = data.first()
        }

        class GetIndicatorValue(val indicator: Indicator, val shift: Int = 0) : DataRequest<IndicatorData>(MT4RequestId.GetIndicatorValue) {
            override fun execute(arrayPointer: AbstractedArrayPointer<Double>) {
                arrayPointer[0] = indicator.ordinal.toDouble()
                arrayPointer[1] = shift.toDouble()
            }

            override fun buildFromResponse(data: Iterable<Double>): IndicatorData = data.iterator().let {
                IndicatorData(
                        value1 = it.next(),
                        value2 = it.next(),
                        value3 = it.next(),
                        value4 = it.next(),
                        value5 = it.next(),
                        value6 = it.next(),
                        value7 = it.next()
                )
            }

        }

        class GetIndicatorNumberOfParams(val indicator: Indicator) : DataRequest<Int>(MT4RequestId.GetIndicatorNumberOfParams) {
            override fun execute(arrayPointer: AbstractedArrayPointer<Double>) {
                arrayPointer[0] = indicator.ordinal.toDouble()
            }

            override fun buildFromResponse(data: Iterable<Double>) = data.first().toInt()
        }
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