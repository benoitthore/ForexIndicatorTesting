package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.Position
import com.bugsnag.example.kotlinmp.lib.wrapper.*
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer
import java.lang.IllegalArgumentException


fun main() {
    val wrapper = MT4APIImpl(MT4HandlerImpl())
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
class MockMT4(val wrapper: MT4API) {
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
        exchange(DATA_REQUEST)
        exchange(POSITION_CONTROL)

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

    // TODO Make this work like so
    /*

    fun onTick() {
        // TODO Return if not new candle
        wrapper.onNewBar()
        exchange(DATA_REQUEST)
        exchange(POSITION_CONTROL)
    }
     */
    private val DATA_REQUEST = 0
    private val POSITION_CONTROL = 1
    fun exchange(mode : Int){
        var isRequesting: Boolean
//        wrapper.setMode(mode)
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