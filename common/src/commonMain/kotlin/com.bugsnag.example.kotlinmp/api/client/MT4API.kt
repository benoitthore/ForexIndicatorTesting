package com.bugsnag.example.kotlinmp.api.client


enum class Indicator {

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


interface MT4API {
    /**
     * Indicate to go to the next candle
     */
    fun close()

    /**
     * Get price for current pair and time frame
     */
    fun getClosePrice(): Double

    /**
     * @param indicator the indicator to call
     * @param index the index for the output value, needs to be less that indicator.outputValues
     */
    fun getIndicatorValue(indicator: Indicator): Double

    /**
     * Return the number of parameters for a specific indicator
     */
    fun getIndicatorNumberOfParams(indicator: Indicator): Int

    /**
     * @return true if position has been opened
     */
    fun openPosition(position: Position): Boolean

    /**
     * @return true if position has been closed
     */
    fun closePosition(position: Position): Boolean

    fun moveStopLoss(toValue: Double): Boolean
}