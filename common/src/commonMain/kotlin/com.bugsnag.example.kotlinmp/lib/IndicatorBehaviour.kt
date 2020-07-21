package com.bugsnag.example.kotlinmp.lib

import com.bugsnag.example.kotlinmp.Log
import com.bugsnag.example.kotlinmp.lib.IndicatorData.Companion.DEFAULT_VALUE
import com.bugsnag.example.kotlinmp.utils.last

sealed class IndicatorBehaviour {
    var lastSignal: Position.Type? = null
        private set

    operator fun invoke(prices: List<Double>, data: List<IndicatorData>): Position.Type? =
            kotlin.runCatching { getSignal(prices, data) }.apply {
                exceptionOrNull()?.let { Log.e(it) }
            }
                    .getOrNull()
                    .also { if (it != null) lastSignal = it }

    protected abstract fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type?

    class ZeroLineCross(value: IndicatorData.() -> Double) : LineCross(0.0, value)

    open class LineCross(val valueToCross: Double, val value: IndicatorData.() -> Double) : IndicatorBehaviour() {
        override fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type? {
            val current = data.last().value()
            val prev = data.last(1).value()
            return if (prev >= valueToCross && current < valueToCross) {
                Position.Type.SHORT
            } else if (prev <= valueToCross && current > valueToCross) {
                Position.Type.LONG
            } else {
                null
            }
        }
    }

    class TwoLineCross(
            val longLine: IndicatorData.() -> Double,
            val shortLine: IndicatorData.() -> Double

    ) : IndicatorBehaviour() {
        override fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type? {
            val currentLong = data.last().longLine()
            val prevLong = data.last(1).longLine()

            val currentShort = data.last().shortLine()
            val prevShort = data.last(1).shortLine()

            if (currentLong > currentShort && prevLong < prevShort) {
                return Position.Type.LONG
            }

            if (currentShort > currentLong && prevShort < prevLong) {
                return Position.Type.SHORT
            }
            return null
        }
    }

    class TwoLineCrossOutsideRange(
            val longLine: IndicatorData.() -> Double,
            val shortLine: IndicatorData.() -> Double,
            val range: ClosedRange<Double>
    ) : IndicatorBehaviour() {
        override fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type? {
            val currentLong = data.last().longLine()
            val prevLong = data.last(1).longLine()

            val currentShort = data.last().shortLine()
            val prevShort = data.last(1).shortLine()
            if (currentLong in range || currentShort in range) {
                return null
            }
            if (currentLong > currentShort && prevLong < prevShort) {
                return Position.Type.LONG
            }

            if (currentShort > currentLong && prevShort < prevLong) {
                return Position.Type.SHORT
            }
            return null
        }
    }

    class TwoLineCrossInsideRange(
            val longLine: IndicatorData.() -> Double,
            val shortLine: IndicatorData.() -> Double,
            val range: ClosedRange<Double>
    ) : IndicatorBehaviour() {
        override fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type? {
            val currentLong = data.last().longLine()
            val prevLong = data.last(1).longLine()

            val currentShort = data.last().shortLine()
            val prevShort = data.last(1).shortLine()
            if (currentLong !in range || currentShort !in range) {
                return null
            }
            if (currentLong > currentShort && prevLong < prevShort) {
                return Position.Type.LONG
            }

            if (currentShort > currentLong && prevShort < prevLong) {
                return Position.Type.SHORT
            }
            return null
        }
    }

    class ActivationIndicator(
            val long: IndicatorData.() -> Double,
            val short: IndicatorData.() -> Double
    ) : IndicatorBehaviour() {

        override fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type? {
            val prevLong = data.last(1).long()
            val currLong = data.last().long()

            val prevShort = data.last(1).short()
            val currShort = data.last().short()

            if (currLong != prevLong && prevLong == 0.0) {
                return Position.Type.LONG
            }

            if (currShort != prevShort && prevShort == 0.0) {
                return Position.Type.SHORT
            }

            return null
        }

    }

    class OnChartAboveOrBelowPrice(val indicatorAboveMeansLong: Boolean, val value: IndicatorData.() -> Double) : IndicatorBehaviour() {

        override fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type? {
            val currPrice = prices.last()
            val prevPrice = prices.last(1)

            val currIndicatorValue = data.last().value()
            val prevIndicatorValue = data.last(1).value()

            var positionType: Position.Type? = null

            if (prevIndicatorValue < prevPrice && currIndicatorValue > currPrice) {
                // indicator going from below to above price
                positionType = Position.Type.SHORT
            } else if (prevIndicatorValue > prevPrice && currIndicatorValue < currPrice) {
                // indicator going from above to below price
                positionType = Position.Type.LONG
            }

            if (indicatorAboveMeansLong) {
                return positionType?.reversed
            }
            return positionType

        }
    }
}
