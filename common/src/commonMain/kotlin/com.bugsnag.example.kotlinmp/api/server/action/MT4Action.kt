package com.bugsnag.example.kotlinmp.api.server.action

import com.bugsnag.example.kotlinmp.api.client.Indicators
import com.bugsnag.example.kotlinmp.api.client.Position
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer

sealed class MT4Action<T>(val actionId: Enum<MT4ActionId>) {
    /**
     * Sets the pointer value based on action
     */
    operator fun invoke(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        actionPointer.value = actionId.ordinal
        execute(arrayPointer)
    }

    protected open fun execute(arrayPointer: AbstractedArrayPointer<Double>) {}
    abstract fun buildFromResponse(data: Iterable<Double>): T

    object Close : MT4Action<Unit>(MT4ActionId.Close) {
        override fun buildFromResponse(data: Iterable<Double>) = Unit
    }

    object GetClosePrice : MT4Action<Unit>(MT4ActionId.GetClosePrice) {
        override fun buildFromResponse(data: Iterable<Double>) = Unit
    }

    class GetIndicatorValue(val indicator: Indicators) : MT4Action<Double>(MT4ActionId.GetIndicatorValue) {
        override fun execute(arrayPointer: AbstractedArrayPointer<Double>) {
            arrayPointer[0] = indicator.ordinal.toDouble()
        }

        override fun buildFromResponse(data: Iterable<Double>) = data.first()
    }

    class GetIndicatorNumberOfParams(val indicator: Indicators) : MT4Action<Int>(MT4ActionId.GetIndicatorNumberOfParams) {
        override fun execute(arrayPointer: AbstractedArrayPointer<Double>) {
            arrayPointer[0] = indicator.ordinal.toDouble()
        }

        override fun buildFromResponse(data: Iterable<Double>) = data.first().toInt()
    }

    sealed class PositionAction(val position: Position, actionId: MT4ActionId) : MT4Action<Boolean>(actionId) {
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

        class OpenPosition(position: Position) : PositionAction(position, MT4ActionId.OpenPosition)
        class UpdatePosition(position: Position) : PositionAction(position, MT4ActionId.UpdatePosition)
        class ClosePosition(position: Position) : PositionAction(position, MT4ActionId.ClosePosition)
    }
}