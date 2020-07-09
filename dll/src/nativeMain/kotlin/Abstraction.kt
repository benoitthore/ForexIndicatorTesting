
val Number.asPositionType: PositionType? get() = kotlin.runCatching { PositionType.values()[toInt()] }.getOrNull()

enum class Indicator {

}
enum class PositionType {
    LONG, SHORT
}

interface NetworkAbstraction {
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
    fun getIndicatorValue(indicator: Indicator, index: Int): Double

    /**
     * Return the number of parameters for a specific indicator
     */
    fun getIndicatorNumberOfParams(indicator: Indicator): Int

    /**
     * @return true if position has been opened
     */
    fun openPosition(type: PositionType, stopLoss: Double, takeProfit: Double? = null): Boolean

    /**
     * @return true if position has been closed
     */
    fun closePosition(): Boolean

    fun moveStopLoss(toValue: Double): Boolean
}