package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.IndicatorBehaviour
import com.bugsnag.example.kotlinmp.lib.IndicatorData
import com.bugsnag.example.kotlinmp.lib.Position
import com.bugsnag.example.kotlinmp.lib.wrapper.EA
import com.bugsnag.example.kotlinmp.utils.throwException


@Suppress("UNREACHABLE_CODE")
fun getPosition(currentPrice: Double, type: Position.Type, magicNumber: Byte, atr: Double, equity: Double, setTP: Boolean): Position? {

    return with(type) {
        when (type) {
            Position.Type.NONE -> null
            Position.Type.LONG, Position.Type.SHORT -> {
                Position(
                        type = type,
                        magicNumber = magicNumber,
                        volume = 1.0,
                        stopLoss = currentPrice _minus 50,
                        takeProfit = currentPrice _plus 50
                )
            }
        }
    }
}

fun getTestEA(): EA {

    var openPosition: Position? = null

    val entrySignal: IndicatorBehaviour = IndicatorBehaviour.ZeroLineCross { value1 }

    return EA.create(listOf(Indicator.ATR)) { equity: List<Double>, closePrices: List<Double>, indicators: Map<Indicator, MutableList<IndicatorData>> ->

        val atr = indicators[Indicator.ATR]?.last()?.value1 ?: throwException("ATR Needed")

        val ma = indicators[Indicator.MovingAverage] ?: throwException("Moving average needed Needed")

        val signal = entrySignal.getSignal(closePrices, ma)

        getPosition(
                currentPrice = closePrices.last(),
                type = signal,
                magicNumber = 2,
                atr = atr,
                equity = equity.last(),
                setTP = true
        )

        emptyList()
    }
}