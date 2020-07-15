package com.bugsnag.example.kotlinmp.lib

import com.bugsnag.example.kotlinmp.utils.throwException

data class Position(
        val type: Type,
        val magicNumber: Byte, //using bytes so MT4 code can do magicNumber + Some value (to run the EA on multiple TF)
        val volume: Double,
        val stopLoss: Double,
        val takeProfit: Double? = null) {
    enum class Type {
        LONG,
        SHORT;

        inline val reversed get() = if (this == LONG) SHORT else LONG

        infix fun Double._plus(other: Number): Double = when (this@Type) {
            LONG -> this + other.toDouble()
            SHORT -> this - other.toDouble()
        }

        infix fun Double._minus(other: Number): Double = when (this@Type) {
            LONG -> this - other.toDouble()
            SHORT -> this + other.toDouble()
        }
    }
}

enum class Symbol {
    EURUSD
}

data class IndicatorData(
        val value1: Double = 0.0,
        val value2: Double = 0.0,
        val value3: Double = 0.0,
        val value4: Double = 0.0,
        val value5: Double = 0.0,
        val value6: Double = 0.0,
        val value7: Double = 0.0
)