package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.Indicator.*
import com.bugsnag.example.kotlinmp.lib.Position
import com.bugsnag.example.kotlinmp.lib.wrapper.*
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer
import java.lang.IllegalArgumentException


fun main() {
    val wrapper = MT4ServiceImpl(MT4DataGathererImpl(ATR, MA20) { closePrices, indicators ->
        val atrValue = (indicators[ATR] ?: error("No ATR value")).last()
        val ma20Value = (indicators[MA20] ?: error("No MA20 value")).last()

        listOf(
                MT4Request.PositionAction.OpenPosition(
                        Position(Position.Type.LONG, 123, ma20Value.value1, ma20Value.value7, takeProfit = closePrices.last())
                )
        )
    })
    val mockMT4 = MockMT4(wrapper)

    mockMT4.onStart()
    mockMT4.onTick()
//    println()
//    mockMT4.onTick()
//    println()
//    mockMT4.onTick()
}

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


    private fun processData(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        when (MT4RequestId.values()[actionPointer.value]) {
            MT4RequestId.GetClosePrice -> {
                arrayPointer[0] = 1000.0
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
                println("""
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
}

private fun iCustom(indicator: String, index: Int = 0): Double = when (indicator) {
    MA20.name -> index.toDouble()
    ATR.name -> Math.random() * 10
    else -> throw IllegalArgumentException("Nope")
}