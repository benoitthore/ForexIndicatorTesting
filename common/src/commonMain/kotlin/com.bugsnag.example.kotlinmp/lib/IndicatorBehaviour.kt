package com.bugsnag.example.kotlinmp.lib

import com.bugsnag.example.kotlinmp.Log
import com.bugsnag.example.kotlinmp.lib.IndicatorData.Companion.DEFAULT_VALUE
import com.bugsnag.example.kotlinmp.utils.last

sealed class IndicatorBehaviour {
    var lastSignal: Position.Type? = null
        private set

    operator fun invoke(prices: List<Double>, data: List<IndicatorData>): Position.Type? =
            kotlin.runCatching { getSignal(prices, data) }.getOrNull().also { if (it != null) lastSignal = it }

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
        override fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type {
            val current1 = data.last().longLine()
            val prev1 = data.last(1).longLine()

            val current2 = data.last().shortLine()
            val prev2 = data.last(1).shortLine()

            TODO()
//            return if (prev >= 0 && current < 0) {
//                Position.Type.SHORT
//            } else if (prev <= 0 && current > 0) {
//                Position.Type.LONG
//            } else {
//                Position.Type.NONE
//            }
        }
    }

    class TwoLineCrossOutsideRange(
            val value1: IndicatorData.() -> Double,
            val value2: IndicatorData.() -> Double,
            val range: ClosedRange<Double>
    ) : IndicatorBehaviour() {
        override fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type {
            TODO()
        }
    }

    class TwoLineCrossInsideRange(
            val value1: IndicatorData.() -> Double,
            val value2: IndicatorData.() -> Double,
            val range: ClosedRange<Double>
    ) : IndicatorBehaviour() {
        override fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type {
            TODO()
        }
    }

    class ActivationIndicator(
            val long: IndicatorData.() -> Double,
            val short: IndicatorData.() -> Double
    ) : IndicatorBehaviour() {

val testWithThis = listOf(
        IndicatorData(value1=0.0, value2=0.0, value3=0.0, value4=0.0, value5=0.0, value6=0.0, value7=0.0),
                IndicatorData(value1=1.0749630000000001, value2=0.0, value3=0.0, value4=0.0, value5=0.0, value6=0.0, value7=0.0)
)
        override fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type? {
            val prevLong = data.last(-1).long()
            val currLong = data.last().long()

            val prevShort = data.last(-1).short()
            val currShort = data.last().short()

            if (currLong != prevLong) {
                return Position.Type.LONG
            }

            if (currShort != prevShort) {
                return Position.Type.SHORT
            }

            return null
        }

    }

    class OutsideRange(
            val value1: IndicatorData.() -> Double,
            val range: ClosedRange<Double>
    ) : IndicatorBehaviour() {
        override fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type {
            TODO()
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