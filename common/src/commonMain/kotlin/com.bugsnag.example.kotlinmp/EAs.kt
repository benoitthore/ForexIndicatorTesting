package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.*
import com.bugsnag.example.kotlinmp.lib.wrapper.EA
import com.bugsnag.example.kotlinmp.lib.wrapper.EAData
import com.bugsnag.example.kotlinmp.lib.wrapper.StartData
import com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange.requests.MT4Request
import com.bugsnag.example.kotlinmp.utils.throwException


fun getPosition(
        currentPrice: Double,
        pipSize: Double,
        type: Position.Type,
        magicNumber: Byte,
        atr: Double,
        equity: Double,
        setTP: Boolean): Position? {
/*
        IN VP'S ALGO THERE IS NO TAKE PROFIT, IT'S DONE MANUALLY AT CANDLE CLOSE
 */

    // https://www.youtube.com/watch?v=ArUtqy-hUxw

    // https://www.youtube.com/watch?v=IvYiJP1elxY
    // For EURUSD 31 * pipSize = 0.0031 ->     31 pips is 0.0031 USD for a volume of
    // Also, PriceToPips = price / pipSize

    // https://www.youtube.com/watch?v=Ft1ITYO8S9Y

    return with(type) {
        val maxLoss = equity * 0.01
        val stopLossPips = atr * 1.5
        val takeProfitPips = atr * 3

        when (type) {
            Position.Type.LONG, Position.Type.SHORT -> {
                Position(
                        type = type,
                        magicNumber = magicNumber,
                        volume = 1.0,
                        stopLoss = currentPrice _minus stopLossPips,

                        //
                        takeProfit = if (setTP) currentPrice _plus takeProfitPips else null
                )
            }
        }
    }
}


data class EAConfig(val entryIndicator: Indicator, val entryIndicatorBehaviour: IndicatorBehaviour)

class VPEA(
        val config: EAConfig = EAConfig(Indicator.ASCTREND_INDICATOR, IndicatorBehaviour.ActivationIndicator(
                short = { value1 },
                long = { value2 }
        ))
) : EA {
    var pipSize: Double? = null
        private set
    var symbol: Symbol? = null
        private set
    var onStartCalled = false
        private set

    private val entryIndicator get() = config.entryIndicator

    override val indicators: List<Indicator>
        get() = listOf(Indicator.ATR, entryIndicator)

    override fun onDataReceived(data: EAData): List<MT4Request.PositionAction> {

        Log.d(data.indicatorsHistory[entryIndicator]?.lastOrNull())

        if (!onStartCalled) {
            throwException("On start hasn't been called")
        }
        val pipSize = pipSize ?: throwException("pipSize needed")
        val symbol = symbol ?: throwException("symbol needed")
        val equity = data.equity.lastOrNull() ?: throwException("equity needed")
        val closePrice = data.closePrices.lastOrNull() ?: throwException("closePrice needed")

        if (data.closePrices.size < 2) return emptyList<MT4Request.PositionAction>().also { Log.d("Needs 2 prices in ${data.closePrices}") }

        val atr = data.indicatorsHistory[Indicator.ATR]?.last()?.value1 ?: throwException("ATR Needed")

        val confirmationIndicatorValue = data.indicatorsHistory[entryIndicator]
                ?: throwException("$entryIndicator needed Needed")

        val entrySignal = config.entryIndicatorBehaviour(data.closePrices, confirmationIndicatorValue)
                ?: return emptyList<MT4Request.PositionAction>().also { Log.d("No trading signal") }

        val position = getPosition(
                currentPrice = closePrice,
                type = entrySignal,
                magicNumber = 2,
                atr = atr,
                equity = equity,
                pipSize = pipSize,
                setTP = true
        )


        return listOfNotNull(position?.toAction()).apply {
            forEach { Log.d("OPEN POSITION ${it.position}") }
        }
    }

    override fun onStart(data: StartData) {
        if (onStartCalled) {
            throwException("On start has already been called")
        }
        pipSize = data.pipSize
        symbol = data.symbol
        onStartCalled = true
    }

}


private fun Position.toAction() = MT4Request.PositionAction.OpenPosition(this)
