package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.*
import com.bugsnag.example.kotlinmp.lib.wrapper.EA
import com.bugsnag.example.kotlinmp.lib.wrapper.EAData
import com.bugsnag.example.kotlinmp.lib.wrapper.StartData
import com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange.requests.MT4Request
import com.bugsnag.example.kotlinmp.utils.roundToDecimal
import com.bugsnag.example.kotlinmp.utils.throwException


fun getPosition(
        currentPrice: Double,
        type: Position.Type,
        magicNumber: Byte,
        atr: Double,
        setTP: Boolean): Position {
/*
        IN VP'S ALGO THERE IS NO TAKE PROFIT, IT'S DONE MANUALLY AT CANDLE CLOSE
 */

    // https://www.youtube.com/watch?v=ArUtqy-hUxw

    // https://www.youtube.com/watch?v=IvYiJP1elxY
    // For EURUSD 31 * pipSize = 0.0031 ->     31 pips is 0.0031 USD for a volume of
    // Also, PriceToPips = price / pipSize

    // https://www.youtube.com/watch?v=Ft1ITYO8S9Y
    fun Double.round() = this.roundToDecimal(4)

    return with(type) {

        val stopLossPips = atr * 1.5
        val takeProfitPips = atr * 2.5



        Position(
                type = type,
                magicNumber = magicNumber,
                accountPercentage = 0.02,
                stopLoss = (currentPrice _minus stopLossPips).round(),
                takeProfit = if (setTP) (currentPrice _plus takeProfitPips).round() else null
        )
    }
}


data class IndicatorConfig(val indicator: Indicator, val behaviour: IndicatorBehaviour)
data class EAConfig(val entry: IndicatorConfig)

class VPEA(
        val config: EAConfig = EAConfig(
                entry = IndicatorConfig(Indicator.ASCTREND_INDICATOR, IndicatorBehaviour.ActivationIndicator(
                        short = { value1 },
                        long = { value2 }
                ))
        )
) : EA {
    var symbol: Symbol? = null
        private set
    var onStartCalled = false
        private set


    override val indicators: List<Indicator>
        get() = listOf(Indicator.ATR, config.entry.indicator)

    private var shouldTrade = true
    override fun onDataReceived(data: EAData): List<MT4Request.PositionAction> {
        if (!shouldTrade) return emptyList<MT4Request.PositionAction>().also { Log.d("Trading disabled") }

        if (!onStartCalled) {
            throwException("On start hasn't been called")
        }
        val symbol = symbol ?: throwException("symbol needed")
        val closePrice = data.closePrices.lastOrNull() ?: throwException("closePrice needed")

        if (data.closePrices.size < 2) return emptyList<MT4Request.PositionAction>().also { Log.d("Needs 2 prices in ${data.closePrices}") }

        val atr = data.indicatorsHistory[Indicator.ATR]?.last()?.value1 ?: throwException("ATR Needed")


        val entrySignal = config.entry.behaviour(
                data.closePrices,
                data.indicatorsHistory[config.entry.indicator]
                        ?: throwException("${config.entry.indicator} needed Needed"))
                ?: return emptyList<MT4Request.PositionAction>()
                        .also {
//                            Log.d("No trading signal")
                        }

        val position = getPosition(
                currentPrice = closePrice,
                type = entrySignal,
                magicNumber = 2,
                atr = atr,
                setTP = true
        )


        return listOfNotNull(position?.toAction()).apply {
            if (isNotEmpty()) {
//                Uncomment to trade 1 time only for debugging purposes
//                shouldTrade = false
            }
            forEach { Log.d("OPEN POSITION ${it.position}") }
        }
    }

    override fun onStart(data: StartData) {
        if (onStartCalled) {
            throwException("On start has already been called")
        }
        symbol = data.symbol
        onStartCalled = true
    }

}


private fun Position.toAction() = MT4Request.PositionAction.OpenPosition(this)
