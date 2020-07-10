package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.api.client.Indicator
import com.bugsnag.example.kotlinmp.api.client.Position
import com.bugsnag.example.kotlinmp.api.server.NewMT4API
import com.bugsnag.example.kotlinmp.api.server.action.MT4RequestId
import com.bugsnag.example.kotlinmp.api.server.NewMT4Wrapper
import com.bugsnag.example.kotlinmp.api.server.NewMT4WrapperImpl
import com.bugsnag.example.kotlinmp.api.server.action.MT4Request
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer
import java.lang.IllegalArgumentException


fun main() {
    val api = object : NewMT4API {

        private val indicatorHistory = mutableMapOf<Indicator, MutableList<Double>>()

        override fun onNewBar(): List<MT4Request<*>> = listOf(
                MT4Request.GetIndicatorValue(Indicator.ATR),
                MT4Request.GetIndicatorValue(Indicator.MA20)
        )

        override fun responseCallback(map: Map<MT4Request<*>, Iterable<Double>>): MT4Request.PositionAction? {
            map.forEach { (request, response) ->
                when (request) {
                    MT4Request.GetClosePrice -> TODO()
                    is MT4Request.GetIndicatorValue -> {
                        indicatorHistory
                                .getOrPut(request.indicator) { mutableListOf() }
                                .add(request.buildFromResponse(response))
                    }
                    is MT4Request.GetIndicatorNumberOfParams -> TODO()
                    is MT4Request.PositionAction.OpenPosition -> TODO()
                    is MT4Request.PositionAction.UpdatePosition -> TODO()
                    is MT4Request.PositionAction.ClosePosition -> TODO()
                }
            }

            return getPosition(indicatorHistory)
        }

        fun getPosition(indicators: Map<Indicator, List<Double>>): MT4Request.PositionAction? {

            val atrValue = (indicators[Indicator.ATR] ?: error("No ATR value")).last()
            val ma20Value = (indicators[Indicator.MA20] ?: error("No MA20 value")).last()

            return MT4Request.PositionAction.OpenPosition(
                    Position(Position.Type.LONG, 123, atrValue, ma20Value)
            )
        }

        override fun actionCallback(success: Boolean) {
            println("actionCallback($success)")
        }

    }
    val wrapper = NewMT4WrapperImpl(api)
    val mockMT4 = MockMT4(wrapper)

    mockMT4.onStart()
    mockMT4.onTick()
}

// TODO TEST IF COROUTINES WORK ON MT4
//
//      test if coroutines work on mt4
//
// TODO TEST IF COROUTINES WORK ON MT4

/* TODO

This class should be converted to MQL4, we first want to test it

Add Channel to MT4WrapperImpl
Create a test scenario where an EA asks for indicator value, result should be 456

 */
class MockMT4(val wrapper: NewMT4Wrapper) {
    private val DEFAULT_VALUE: Double = -1.0
    val actionPointer = DEFAULT_VALUE.toInt().p
    val arrayPointer = MutableList(10) { DEFAULT_VALUE }.p

    fun onStart() {
        // TODO
//        wrapper.onStart(actionPointer, arrayPointer)
    }

    fun onTick() {
        // TODO Return if not new candle

        // Starts the process in the EA
        wrapper.onNewBar()

        // Communication loop between EA and mt4
        var isRequesting: Boolean
        do {
            //reset (probably not needed)
            reset(arrayPointer)
            actionPointer.value = DEFAULT_VALUE.toInt()

            isRequesting = wrapper.request(actionPointer, arrayPointer)
            if (isRequesting) {
                processData(actionPointer, arrayPointer)
                wrapper.response(actionPointer, arrayPointer)
            }
        } while (isRequesting)

        wrapper.action(actionPointer, arrayPointer)
        processData(actionPointer, arrayPointer)
        wrapper.actionResponse(actionPointer, arrayPointer)
    }

    private fun processData(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        when (MT4RequestId.values()[actionPointer.value]) {
            MT4RequestId.GetClosePrice -> TODO()
            MT4RequestId.GetIndicatorValue -> {
                val indicator = indicatorIDToString(arrayPointer[0])
                reset(arrayPointer) //reset (probably not needed)
                arrayPointer[0] = iCustom(indicator)
            }
            MT4RequestId.GetIndicatorNumberOfParams -> TODO()
            MT4RequestId.OpenPosition -> {
                val type = arrayPointer[0]
                val magicNumber = arrayPointer[1]
                val volume = arrayPointer[2]
                val stopLoss = arrayPointer[3]
                val takeProfit = arrayPointer[4]
                println("""
                    Opening position:
                    type=$type
                    magicNumber=$magicNumber
                    volume=$volume
                    stopLoss=$stopLoss
                    takeProfit=$takeProfit
                """.trimIndent())

            }
            MT4RequestId.ClosePosition -> TODO()
            MT4RequestId.UpdatePosition -> TODO()
        }

    }

    // Harder on MQL4
    private fun indicatorIDToString(d: Double): String = Indicator.values()[d.toInt()].name

    private fun reset(arrayPointer: AbstractedArrayPointer<Double>) {
        for (i in 0 until arrayPointer.size) {
            arrayPointer[i] = DEFAULT_VALUE
        }
    }
}

private fun iCustom(indicator: String): Double = when (indicator) {
    Indicator.MA20.name -> 20.0
    Indicator.ATR.name -> 14.0
    else -> throw IllegalArgumentException("Nope")
}