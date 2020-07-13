package example

import com.bugsnag.example.kotlinmp.Strategy
import com.bugsnag.example.kotlinmp.getTestEA
import com.bugsnag.example.kotlinmp.lib.wrapper.MT4Service
import com.bugsnag.example.kotlinmp.platformId
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer
import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.DoubleVar
import kotlinx.cinterop.IntVar

var i = 0

@CName(externName = "testFun", shortName = "testFun")
fun testFun(): Int = i++


@CName(externName = "testFun", shortName = "testFun")
fun onNewBar() =
        Impl.onNewBar()

@CName(externName = "goToActionMode", shortName = "goToActionMode")
fun goToActionMode() =
        Impl.goToActionMode()

@CName(externName = "request", shortName = "request")
fun request(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>): Boolean =
        Impl.request(actionPointer, arrayPointer)

@CName(externName = "response", shortName = "response")
fun response(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) =
        Impl.response(actionPointer, arrayPointer)


private val Impl = Strategy.get(getTestEA())



