package com.bugsnag.example.kotlinmp.codegeneration


private val String.enumName
    get() = replace('-', '_').toUpperCase().let {
        if (it.toCharArray().firstOrNull()?.isDigit() == true) "_$it" else it
    }

private fun generateMappingFunction() {

    list.map {
        it to it.enumName
    }.forEach { (fileName, enumName) ->
        println(
                "if (indicator == $enumName) { return $fileName; } "
        )
    }

    println("Alert(\"Indicator not found\");")


}

private fun generateEnum() {
    list.forEach {
        println("${it.enumName},")
    }
}

fun main() {
//    generateMappingFunction()
    generateEnum()
}


val list = """
    3d-oscillator-2
    absolute-strength-indicator
    alf-of-oscillator
    alternative-cci-indicator
    amka-indicator
    aroon-indicator
    asctrend-indicator
    atr
    averages-rainbow-indicator
    balance-of-market-power
    bears-vs-bulls-indicator
    blau-jurik-eco-indicator
    blau-t3-ergodic-candlestick-oscillator
    candle-ratio-indicator
    cci-of-average-indicator
    cci-t3-indicador
    center-of-gravity-extended-indicator
    chaikin-volatility-indicator
    chandelier-exit-indicator
    chandes-dmi-indicator
    chanellon-parabolic-indicator
    choppy-market-index
    cmo-indicator
    color-parabolic-indicator
    comparative-adx-indicator
    coppock-indicator
    cronex-t-rsi-bbsw-indicador
    cumulative-volume-indicator
    cyber-cycle-indicator
    d-index-indicator
    derivative-oscillator
    directional-momentum-of-ema
    disparity-index-indicator
    dpo-bar-indicator
    drunkard-walk-indicator
    dsl-dmi-oscillator
    dsl-synthetic-ema-momentum
    dss-bressert-indicador
    dss-ratio-indicator
    dynamic-trend-indicator
    elliot-oscillator
    ema-angle-indicator
    finite-volume-elements
    fish-indicator
    fisher-org-indicator
    fisher-rvi-indicador
    flat-indikator
    gann-high-low-activator-mtf
    hurst-oscillator
    is7n-trend-indicator
    ivar-indicator
    kdj-indicator
    laguerre-rsi-with-laguerre-filter-2
    mama-indicator
    mfi-indicator
    nonlagdot-indicator
    notis-indicator
    obos-indicator
    oracle-indicator
    pfe2-indicator
    power-trend-indicator
    prevailing-trend-indicator
    price-channel-stop
    qqe-of-velocity-mtf-indicator
    ravi-ifish-indicateur
    repulse-indicator
    roc-indicateur
    schaff-trend-cci-indikator
    spearman-rank-auto-correlation
    split-ma-indicator
    std-trend-envelopes-of-averages
    super-sar-indicator
    super-trend-dot-indicador
    tim-morris-ma-indicator
    tma-indicator
    trend-confirmation-index-indicator
    trend-score-oscillator
    trendmagic-indicator
    tsi-indicator
    ttm-squeeze-indicator
    value-charts-indicator
    vidya-indicateur
    vsa-candle-indicator
    watr-indikator
""".trimIndent().split("\n")












