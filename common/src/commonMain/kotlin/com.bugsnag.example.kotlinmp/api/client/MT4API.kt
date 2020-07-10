package com.bugsnag.example.kotlinmp.api.client


enum class Indicators {
    ATR

}

// Don't use runCatching
//val Number.asPositionType: PositionType? get() = kotlin.runCatching { PositionType.values()[toInt()] }.getOrNull()

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

interface MT4API {

    var onNewCandle: (() -> Unit)?
        get() = null
        set(value) {}
    var onStart: ((OnStartData) -> Unit)?
        get() = null
        set(value) {}

    /**
     * Indicate to go to the next candle
     */
    suspend fun close()

    /**
     * Get price for current pair and time frame
     */
    suspend fun getClosePrice(): Double

    /**
     * @param indicator the indicator to call
     * @param index the index for the output value, needs to be less that indicator.outputValues
     */
    suspend fun getIndicatorValue(indicator: Indicators): Double

    /**
     * Return the number of parameters for a specific indicator
     */
    suspend fun getIndicatorNumberOfParams(indicator: Indicators): Int

    /**
     * @return true if the position has been opened
     */
    suspend fun openPosition(position: Position): Boolean

    /**
     * @return true if the position has been updated
     */
    suspend fun updatePosition(position: Position): Boolean

    /**
     * @return true if the position has been closed
     */
    suspend fun closePosition(position: Position): Boolean

}
