package com.bugsnag.example.kotlinmp.lib

// Don't use runCatching
//val Number.asPositionType: PositionType? get() = kotlin.runCatching { PositionType.values()[toInt()] }.getOrNull()

enum class Indicator {
    ATR,MA20
}
data class IndicatorValue(
        val double: Double
)
data class Position(
        val type: Type,
        val magicNumber: Number,
        val volume: Double,
        val stopLoss: Double,
        val takeProfit: Double? = null) {

    enum class Type {
        LONG, SHORT
    }

}

enum class Symbol {
    EURUSD
}

class OnStartData(val symbol: Symbol)
