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


class VPEA(
        val entryIndicator: IndicatorBehaviour = IndicatorBehaviour.OnChartAboveOrBelowPrice(indicatorAboveMeansLong = false) { value1 }
) : EA {
    var pipSize: Double? = null
        private set
    var symbol: Symbol? = null
        private set
    var onStartCalled = false
        private set


    override val indicators: List<Indicator>
        get() = listOf(Indicator.ATR)

    override fun onDataReceived(data: EAData): List<MT4Request.PositionAction> {

        if (!onStartCalled) {
            throwException("On start hasn't been called")
        }
        val pipSize = pipSize ?: throwException("pipSize needed")
        val symbol = symbol ?: throwException("symbol needed")
        val equity = data.equity.lastOrNull() ?: throwException("equity needed")
        val closePrice = data.closePrices.lastOrNull() ?: throwException("closePrice needed")

        if (data.closePrices.size < 2) return emptyList()

        val atr = data.indicatorsHistory[Indicator.ATR]?.last()?.value1 ?: throwException("ATR Needed")

        val ma = data.indicatorsHistory[Indicator.MA] ?: throwException("Moving average needed Needed")

        val entrySignal = entryIndicator(data.closePrices, ma) ?: return emptyList()

        val position = getPosition(
                currentPrice = closePrice,
                type = entrySignal,
                magicNumber = 2,
                atr = atr,
                equity = equity,
                pipSize = pipSize,
                setTP = true
        )


        return listOfNotNull(position?.toAction())
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
