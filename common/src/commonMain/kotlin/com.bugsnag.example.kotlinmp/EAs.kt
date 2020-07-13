package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.*
import com.bugsnag.example.kotlinmp.lib.wrapper.EA
import com.bugsnag.example.kotlinmp.lib.wrapper.EAData
import com.bugsnag.example.kotlinmp.lib.wrapper.requests.MT4Request
import com.bugsnag.example.kotlinmp.utils.throwException


@Suppress("UNREACHABLE_CODE")
fun getPosition(
        currentPrice: Double,
        pipsToPrice: Double,
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

    val entrySignal: IndicatorBehaviour = IndicatorBehaviour.ZeroLineCross { value1 }

    var pipsToPrice: Double? = null
    var symbol: Symbol? = null
    return EA.create(listOf(Indicator.ATR), {
        pipsToPrice = it.pipsToPrice
        symbol = it.symbol
    }) { (equity: List<Double>, closePrices: List<Double>, indicators: Map<Indicator, MutableList<IndicatorData>>) ->

        val pipsToPrice = pipsToPrice ?: throwException("Price per pip needed")
        val symbol = symbol ?: throwException("Price per pip needed")

        if (closePrices.size < 2) return@create emptyList()


        val atr = indicators[Indicator.ATR]?.last()?.value1 ?: throwException("ATR Needed")

        val ma = indicators[Indicator.MA] ?: throwException("Moving average needed Needed")

        val signal = entrySignal(closePrices, ma) ?: return@create emptyList()


       val position =  getPosition(
                currentPrice = closePrices.last(),
                type = signal,
                magicNumber = 2,
                atr = atr,
                equity = equity.last(),
                pipsToPrice = pipsToPrice,
                setTP = true
        )


        listOfNotNull(position?.toAction())
    }
}

private fun Position.toAction() = MT4Request.PositionAction.OpenPosition(this)
