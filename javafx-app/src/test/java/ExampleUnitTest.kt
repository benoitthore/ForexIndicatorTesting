import com.bugsnag.example.kotlinmp.getTestEA
import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.IndicatorData
import com.bugsnag.example.kotlinmp.lib.Position
import com.bugsnag.example.kotlinmp.lib.Symbol
import com.bugsnag.example.kotlinmp.lib.wrapper.EAData
import com.bugsnag.example.kotlinmp.lib.wrapper.StartData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val ea = getTestEA()
        ea.onStart(StartData(Symbol.EURUSD, 10.0))
        val indicatorsHistory = mutableMapOf<Indicator, MutableList<IndicatorData>>()

        val atrList = mutableListOf<IndicatorData>()
        indicatorsHistory[Indicator.ATR] = atrList

        val maList = mutableListOf<IndicatorData>()
        indicatorsHistory[Indicator.MA] = maList

        val priceList = mutableListOf<Double>()

        val equity = listOf(1000.0);

        fun add(ma: Number,price:Number): List<Position> {
            priceList += price.toDouble()
            atrList += IndicatorData(14.0)
            maList += IndicatorData(ma.toDouble())
            return ea.onDataReceived(EAData(equity, priceList, indicatorsHistory)).map { it.position }
        }


        add(10,20).shouldBeEmpty() // not enough data
        add(30,20).first().type shouldEqual Position.Type.LONG
        add(10,20).first().type shouldEqual Position.Type.SHORT
        add(10,20).shouldBeEmpty() // don't give signal twice

    }
}


