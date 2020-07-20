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
        val value1: Double = DEFAULT_VALUE,
        val value2: Double = DEFAULT_VALUE,
        val value3: Double = DEFAULT_VALUE,
        val value4: Double = DEFAULT_VALUE,
        val value5: Double = DEFAULT_VALUE,
        val value6: Double = DEFAULT_VALUE,
        val value7: Double = DEFAULT_VALUE
){
    companion object{
        const val DEFAULT_VALUE : Double = 0.0
        val value1: IndicatorData.() -> Double = { value1 }
        val value2: IndicatorData.() -> Double = { value2 }
        val value3: IndicatorData.() -> Double = { value3 }
        val value4: IndicatorData.() -> Double = { value4 }
        val value5: IndicatorData.() -> Double = { value5 }
        val value6: IndicatorData.() -> Double = { value6 }
        val value7: IndicatorData.() -> Double = { value7 }
    }
}