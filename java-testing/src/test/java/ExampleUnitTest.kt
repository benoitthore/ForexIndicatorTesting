import com.bugsnag.example.kotlinmp.EAConfig
import com.bugsnag.example.kotlinmp.Strategy
import com.bugsnag.example.kotlinmp.VPEA
import com.bugsnag.example.kotlinmp.lib.*
import com.bugsnag.example.kotlinmp.lib.wrapper.EAData
import com.bugsnag.example.kotlinmp.lib.wrapper.StartData
import com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange.MT4ClientImpl
import org.junit.jupiter.api.Test

class ExampleUnitTest {

    // https://github.com/mockk/mockk
    @Test
    fun test1() {
        val ea = VPEA(
                EAConfig(Indicator.CUSTOM_MOVING_AVERAGE, entryIndicatorBehaviour = IndicatorBehaviour.OnChartAboveOrBelowPrice(indicatorAboveMeansLong = true) { value1 })
        )
        ea.onStart(StartData(Symbol.EURUSD, 10.0))
        val indicatorsHistory = mutableMapOf<Indicator, MutableList<IndicatorData>>()

        val atrList = mutableListOf<IndicatorData>()
        indicatorsHistory[Indicator.ATR] = atrList

        val maList = mutableListOf<IndicatorData>()
        indicatorsHistory[Indicator.CUSTOM_MOVING_AVERAGE] = maList

        val priceList = mutableListOf<Double>()

        val equity = listOf(1000.0)

        fun add(ma: Number, price: Number): List<Position> {
            priceList += price.toDouble()
            atrList += IndicatorData(14.0)
            maList += IndicatorData(ma.toDouble())
            return ea.onDataReceived(EAData(equity, priceList, indicatorsHistory)).map { it.position }
        }

        add(10, 20).shouldBeEmpty() // not enough data
        add(30, 20).first().type shouldEqual Position.Type.LONG
        add(10, 20).first().type shouldEqual Position.Type.SHORT
        add(10, 30).shouldBeEmpty() // don't give signal twice
    }

    @Test
    fun testActivationIndicator() {
        val vpea = (Strategy.getVPEA(73) as MT4ClientImpl).handler.ea as VPEA
        vpea.config.entryIndicator shouldEqual Indicator.ASCTREND_INDICATOR
        vpea.config.entryIndicatorBehaviour.invoke(emptyList(),
                listOf(
                        IndicatorData(0.0), IndicatorData(1.0)
                )
        ) shouldEqual Position.Type.LONG
    }

}


