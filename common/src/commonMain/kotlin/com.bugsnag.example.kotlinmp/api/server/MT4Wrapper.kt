package com.bugsnag.example.kotlinmp.api.server

import com.bugsnag.example.kotlinmp.api.client.*
import com.bugsnag.example.kotlinmp.api.server.action.MT4Action
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer

interface MT4Wrapper {

    fun onNewCandle()
    fun fillData(actionPointer: AbstractedPointer<Int>,
                 arrayPointer: AbstractedArrayPointer<Double>)

    fun onDataReceived(actionPointer: AbstractedPointer<Int>,
                       arrayPointer: AbstractedArrayPointer<Double>)

    fun onStart(actionPointer: AbstractedPointer<Int>,
                arrayPointer: AbstractedArrayPointer<Double>)

}


class MT4WrapperImpl : MT4Wrapper, MT4API {

    var action: MT4Action<*>? = null
    private suspend fun <T> pushAction(actionData: MT4Action<*>): T {
        val data: Iterable<Double> = TODO("suspends on Channel.next()")
        return actionData.buildFromResponse(data) as T
    }

    override fun onNewCandle() {
        onNewCandle?.invoke()
    }

    override fun fillData(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        // Uncomment below to test
//        actionPointer.value = arrayPointer.size
//        arrayPointer.forEachIndexed { index, value ->
//            arrayPointer[index] = value + 1
//        }

        action?.invoke(actionPointer, arrayPointer)
        action = null
    }

    override fun onDataReceived(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        TODO("Push to channel")
    }

    override fun onStart(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        onStart?.invoke(OnStartData(Symbol.EURUSD)) // TODO
    }


    override suspend fun close(): Unit = pushAction(MT4Action.Close)

    override suspend fun getClosePrice(): Double = pushAction(MT4Action.GetClosePrice)

    override suspend fun getIndicatorValue(indicator: Indicators): Double = pushAction(MT4Action.GetIndicatorValue(indicator))

    override suspend fun getIndicatorNumberOfParams(indicator: Indicators): Int = pushAction(MT4Action.GetIndicatorNumberOfParams(indicator))

    override suspend fun openPosition(position: Position): Boolean = pushAction(MT4Action.PositionAction.OpenPosition(position))

    override suspend fun updatePosition(position: Position): Boolean = pushAction(MT4Action.PositionAction.UpdatePosition(position))

    override suspend fun closePosition(position: Position): Boolean = pushAction(MT4Action.PositionAction.ClosePosition(position))

}