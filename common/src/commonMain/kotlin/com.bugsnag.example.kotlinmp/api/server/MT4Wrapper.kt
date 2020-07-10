package com.bugsnag.example.kotlinmp.api.server

import com.bugsnag.example.kotlinmp.api.client.*
import com.bugsnag.example.kotlinmp.api.server.action.MT4Request
import com.bugsnag.example.kotlinmp.myRunBlocking
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer
import kotlinx.coroutines.channels.Channel

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

    private val channel = Channel<Iterable<Double>>(1)

    var action: MT4Request<*>? = null
    private suspend fun <T> pushAction(actionData: MT4Request<*>): T {
        action = actionData
        val data: Iterable<Double> = channel.receive()
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

        action?.buildRequest(actionPointer, arrayPointer)

    }

    override fun onDataReceived(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
//        val response = action?.buildFromResponse(arrayPointer)
        myRunBlocking { channel.send(arrayPointer) }
    }

    override fun onStart(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        onStart?.invoke(OnStartData(Symbol.EURUSD)) // TODO
    }


    override suspend fun close(): Unit = pushAction(MT4Request.Close)

    override suspend fun getClosePrice(): Double = pushAction(MT4Request.GetClosePrice)

    override suspend fun getIndicatorValue(indicator: Indicators): Double = pushAction(MT4Request.GetIndicatorValue(indicator))

    override suspend fun getIndicatorNumberOfParams(indicator: Indicators): Int = pushAction(MT4Request.GetIndicatorNumberOfParams(indicator))

    override suspend fun openPosition(position: Position): Boolean = pushAction(MT4Request.PositionAction.OpenPosition(position))

    override suspend fun updatePosition(position: Position): Boolean = pushAction(MT4Request.PositionAction.UpdatePosition(position))

    override suspend fun closePosition(position: Position): Boolean = pushAction(MT4Request.PositionAction.ClosePosition(position))

}