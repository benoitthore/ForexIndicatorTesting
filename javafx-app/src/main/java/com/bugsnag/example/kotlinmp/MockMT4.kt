package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.wrapper.MT4RequestId
import com.bugsnag.example.kotlinmp.lib.wrapper.MT4Service
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer
import java.lang.IllegalArgumentException

/**
This class should be converted to MQL4, we first want to test it

 */
class MockMT4(val service: MT4Service) {
    private val DEFAULT_VALUE: Double = -1.0
    val actionPointer = DEFAULT_VALUE.toInt().p
    val arrayPointer = MutableList(10) { DEFAULT_VALUE }.p

    fun onStart() {
        // TODO
//        wrapper.onStart(actionPointer, arrayPointer)
    }

    fun onTick() {
        // TODO Return if not new candle

        service.onNewBar()
        exchange()

        service.goToActionMode()
        exchange()

    }

    fun exchange() {
        var isRequesting: Boolean
        do {
            //reset (probably not needed)
            reset(arrayPointer)
            actionPointer.value = DEFAULT_VALUE.toInt()

            isRequesting = service.request(actionPointer, arrayPointer)
            if (isRequesting) {
                processData(actionPointer, arrayPointer)
                service.response(actionPointer, arrayPointer)
            }
        } while (isRequesting)
    }


    var i = 0
    private fun processData(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        when (MT4RequestId.values()[actionPointer.value]) {
            MT4RequestId.GetClosePrice -> {
                arrayPointer[0] = 1000.0 + i++
            }
            MT4RequestId.GetIndicatorValue -> {
                val indicator = indicatorIDToString(arrayPointer[0])
                reset(arrayPointer)
                for (i in 0 until 7) {
                    arrayPointer[i] = iCustom(indicator, i)
                }
            }
            MT4RequestId.GetIndicatorNumberOfParams -> TODO()
            MT4RequestId.OpenPosition -> {
                val type = arrayPointer[0]
                val magicNumber = arrayPointer[1]
                val volume = arrayPointer[2]
                val stopLoss = arrayPointer[3]
                val takeProfit = arrayPointer[4]
                log("""
                    Opening position:
                    type=$type
                    magicNumber=$magicNumber
                    volume=$volume
                    stopLoss=$stopLoss
                    takeProfit=$takeProfit
                """.trimIndent())

                reset(arrayPointer)
                arrayPointer[0] = 1.0
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

    private fun log(message : String){
        println("MOCK MT4: $message")
    }
}


private fun iCustom(indicator: String, index: Int = 0): Double = when (indicator) {
    Indicator.MovingAverage.name -> index.toDouble()
    Indicator.ATR.name -> Math.random() * 10
    else -> throw IllegalArgumentException("Nope")
}