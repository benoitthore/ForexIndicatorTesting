package com.bugsnag.example.kotlinmp.lib

import com.bugsnag.example.kotlinmp.utils.last

// Don't use runCatching
//val Number.asPositionType: PositionType? get() = kotlin.runCatching { PositionType.values()[toInt()] }.getOrNull()


enum class Indicator {
    ATR, MovingAverage;
}

sealed class IndicatorBehaviour {
    var lastSignal: Position.Type = Position.Type.NONE
    private set
    operator fun invoke(prices: List<Double>, data: List<IndicatorData>): Position.Type =
            getSignal(prices, data).apply { lastSignal = this }

    protected abstract fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type

    class ZeroLineCross(val value: IndicatorData.() -> Double) : IndicatorBehaviour() {
        override fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type {
            val current = data.last().value()
            val prev = data.last(1).value()
            return if (prev >= 0 && current < 0) {
                Position.Type.SHORT
            } else if (prev <= 0 && current > 0) {
                Position.Type.LONG
            } else {
                Position.Type.NONE
            }
        }
    }

    class TwoLineCross(
            val value1: IndicatorData.() -> Double,
            val value2: IndicatorData.() -> Double

    ) : IndicatorBehaviour() {
        override fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type {
            val current1 = data.last().value1()
            val prev1 = data.last(1).value1()

            val current2 = data.last().value2()
            val prev2 = data.last(1).value2()

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

    class OnChartAboveOrBelowPrice(val value: IndicatorData.() -> Double) : IndicatorBehaviour() {
        override fun getSignal(prices: List<Double>, data: List<IndicatorData>): Position.Type {
            if (data.last().value() > prices.last()) {
                return Position.Type.LONG
            } else {
                return Position.Type.SHORT
            }
        }

    }
}
