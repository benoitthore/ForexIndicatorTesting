package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.lib.Indicator
import com.bugsnag.example.kotlinmp.lib.Indicator.*
import com.bugsnag.example.kotlinmp.lib.IndicatorData
import com.bugsnag.example.kotlinmp.lib.Position
import com.bugsnag.example.kotlinmp.lib.wrapper.EA
import com.bugsnag.example.kotlinmp.lib.wrapper.EAWrapper
import com.bugsnag.example.kotlinmp.lib.wrapper.MT4Service
import com.bugsnag.example.kotlinmp.lib.wrapper.MT4ServiceImpl


object Strategy {
    fun get(ea : EA): MT4Service = MT4ServiceImpl(EAWrapper(ea))
}