package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.IndicatorBehaviour
import com.bugsnag.example.kotlinmp.lib.IndicatorData
import com.bugsnag.example.kotlinmp.lib.IndicatorData.Companion.value3
import com.bugsnag.example.kotlinmp.lib.IndicatorData.Companion.value4
import com.bugsnag.example.kotlinmp.lib.wrapper.EA
import com.bugsnag.example.kotlinmp.lib.wrapper.MT4HandlerImpl
import com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange.MT4Client
import com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange.MT4ClientImpl


object Strategy {
    fun get(ea: EA): MT4Client = MT4ClientImpl(MT4HandlerImpl(ea))
    fun getVPEA(config: EAConfig) = get(VPEA(config))
    fun getVPEA(configIndex: Int) = getVPEA(Builder().configList[configIndex])


    class Builder {

        val value1 = IndicatorData.value1
        val value2 = IndicatorData.value2

        val indicatorBehaviourList: List<IndicatorBehaviour> = listOf(
                IndicatorBehaviour.TwoLineCross(value1, value2),
                IndicatorBehaviour.TwoLineCross(value2, value1),

                IndicatorBehaviour.ZeroLineCross(value1),

                IndicatorBehaviour.ActivationIndicator(value1, value2),
                IndicatorBehaviour.ActivationIndicator(value2, value1),
                IndicatorBehaviour.ActivationIndicator(value3, value4),
                IndicatorBehaviour.ActivationIndicator(value4, value3),

                IndicatorBehaviour.OnChartAboveOrBelowPrice(true, value1),
                IndicatorBehaviour.OnChartAboveOrBelowPrice(false, value1),
                IndicatorBehaviour.OnChartAboveOrBelowPrice(true, value2),
                IndicatorBehaviour.OnChartAboveOrBelowPrice(false, value2),
                IndicatorBehaviour.OnChartAboveOrBelowPrice(true, value3),
                IndicatorBehaviour.OnChartAboveOrBelowPrice(false, value3),
                IndicatorBehaviour.OnChartAboveOrBelowPrice(true, value4),
                IndicatorBehaviour.OnChartAboveOrBelowPrice(false, value4)
        )
        val configList: List<EAConfig> = Indicator.values().flatMap { indicator ->
            indicatorBehaviourList.map { indicatorBehaviour ->
                EAConfig(entry = IndicatorConfig(indicator, indicatorBehaviour))
            }
        }
    }
}