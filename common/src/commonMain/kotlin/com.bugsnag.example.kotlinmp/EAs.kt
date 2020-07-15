package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.*
import com.bugsnag.example.kotlinmp.lib.wrapper.EA
import com.bugsnag.example.kotlinmp.lib.wrapper.EAData
import com.bugsnag.example.kotlinmp.lib.wrapper.StartData
import com.bugsnag.example.kotlinmp.lib.wrapper.requests.MT4Request
import com.bugsnag.example.kotlinmp.utils.throwException


@Suppress("UNREACHABLE_CODE")
fun getPosition(
        currentPrice: Double,
        pipSize: Double,
        type: Position.Type,
        magicNumber: Byte,
        atr: Double,
        equity: Double,
        setTP: Boolean): Position? {


    return with(type) {
        val maxLoss = equity * 0.01
        val stopLossPips = atr * 1.5
        val takeProfitPips = atr * 1

        when (type) {
            Position.Type.LONG, Position.Type.SHORT -> {
                Position(
                        type = type,
                        magicNumber = magicNumber,
                        volume = 1.0,
                        stopLoss = currentPrice _minus stopLossPips,
                        takeProfit = if (setTP) currentPrice _plus takeProfitPips else null
                )
            }
        }
    }
}

private fun openBuy(values: EAData): Boolean {
    return false
}

private fun openSell(values: EAData): Boolean {
    return false
}

private fun closeBuy(values: EAData): Boolean {
    return false
}

private fun closeSell(values: EAData): Boolean {
    return false
}

fun getTestEA(): EA {

    var openPosition: Position? = null

    val entryIndicator: IndicatorBehaviour = IndicatorBehaviour.OnChartAboveOrBelowPrice { value1 }

    var pipSize: Double? = null
    var symbol: Symbol? = null
    var onStartCalled = false

    return EA.create(listOf(Indicator.ATR), {
        if (onStartCalled) {
            throwException("On start has already been called")
        }
        pipSize = it.pipSize
        symbol = it.symbol
        onStartCalled = true
    }) { (equity: List<Double>, closePrices: List<Double>, indicators: Map<Indicator, MutableList<IndicatorData>>) ->

//        log("<EA>")
        run {
            if (!onStartCalled) {
                throwException("On start hasn't been called")
            }
            val pipSize = pipSize ?: throwException("pipSize needed")
            val symbol = symbol ?: throwException("symbol needed")
            val equity = equity.lastOrNull() ?: throwException("equity needed")
            val closePrice = closePrices.lastOrNull() ?: throwException("closePrice needed")

            if (closePrices.size < 2) return@run emptyList<MT4Request.PositionAction>()


            val atr = indicators[Indicator.ATR]?.last()?.value1 ?: throwException("ATR Needed")

            val ma = indicators[Indicator.MA] ?: throwException("Moving average needed Needed")

            val entrySignal = entryIndicator(closePrices, ma) ?: return@create emptyList()

            val position = getPosition(
                    currentPrice = closePrice,
                    type = entrySignal,
                    magicNumber = 2,
                    atr = atr,
                    equity = equity,
                    pipSize = pipSize,
                    setTP = true
            )


            listOfNotNull(position?.toAction())
        }.also {
//            log("</EA>")
        }
    }
}

private fun Position.toAction() = MT4Request.PositionAction.OpenPosition(this)
