package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.api.client.Indicators
import com.bugsnag.example.kotlinmp.api.server.action.MT4ActionId
import com.bugsnag.example.kotlinmp.api.server.MT4Wrapper
import com.bugsnag.example.kotlinmp.api.server.MT4WrapperImpl
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer


fun main() {
    val wrapper = MT4WrapperImpl()
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
class MockMT4(val wrapper: MT4Wrapper) {
    private val DEFAULT_ARRAY_VALUE: Double = 0.0
    private val DEFAULT_ACTION_VALUE: Int = MT4ActionId.Close.ordinal
    val actionPointer = DEFAULT_ACTION_VALUE.p
    val arrayPointer = MutableList(10) { DEFAULT_ARRAY_VALUE }.p

    fun onStart() {
        wrapper.onStart(actionPointer, arrayPointer)
    }

    fun onTick() {
        // TODO Return if not new candle

        // Starts the process in the EA
        wrapper.onNewCandle()

        // Communication loop between EA and mt4
        do {
            //reset (probably not needed)
            reset(arrayPointer)
            actionPointer.value = DEFAULT_ACTION_VALUE

            wrapper.fillData(actionPointer, arrayPointer)
            processData(actionPointer, arrayPointer)
            wrapper.onDataReceived(actionPointer, arrayPointer)


        } while (actionPointer.value != MT4ActionId.Close.ordinal)
    }

    private fun processData(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        when (MT4ActionId.values()[actionPointer.value]) {
            MT4ActionId.Close -> {
                println("Closing")
            }
            MT4ActionId.GetClosePrice -> TODO()
            MT4ActionId.GetIndicatorValue -> {
                val indicator = indicatorIDToString(arrayPointer[0])
                reset(arrayPointer) //reset (probably not needed)
                arrayPointer[0] = iCustom(indicator)
            }
            MT4ActionId.GetIndicatorNumberOfParams -> TODO()
            MT4ActionId.OpenPosition -> TODO()
            MT4ActionId.ClosePosition -> TODO()
            MT4ActionId.UpdatePosition -> TODO()
        }

    }

    // Harder on MQL4
    private fun indicatorIDToString(d: Double): String = Indicators.values()[d.toInt()].name

    private fun reset(arrayPointer: AbstractedArrayPointer<Double>) {
        for (i in 0 until arrayPointer.size) {
            arrayPointer[i] = DEFAULT_ARRAY_VALUE
        }
    }
}

private fun iCustom(indicator: String): Double = 456.0