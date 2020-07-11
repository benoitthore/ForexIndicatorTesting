package com.bugsnag.example.kotlinmp.lib

// Don't use runCatching
//val Number.asPositionType: PositionType? get() = kotlin.runCatching { PositionType.values()[toInt()] }.getOrNull()

enum class Indicator {
    ATR, MA20
}

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


data class IndicatorData(
        val value1: Double,
        val value2: Double,
        val value3: Double,
        val value4: Double,
        val value5: Double,
        val value6: Double,
        val value7: Double
)