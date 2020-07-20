package example

import com.bugsnag.example.kotlinmp.Log
import com.bugsnag.example.kotlinmp.Strategy
import com.bugsnag.example.kotlinmp.VPEA
import com.bugsnag.example.kotlinmp.lib.wrapper.EA
import com.bugsnag.example.kotlinmp.lib.wrapper.dataexchange.MT4Client
import com.bugsnag.example.kotlinmp.utils.getOrLogException
import com.bugsnag.example.kotlinmp.utils.throwException
import kotlinx.cinterop.*

var i = 0


@CName(externName = "testArray", shortName = "testArray")
fun testArray(actionPointer: CPointer<IntVar>, arrayPointer: CArrayPointer<DoubleVar>, arraySize: Int): Boolean {
    val actionPointer = actionPointer.abstractedVarPointer()
    actionPointer.value = 42
    val array = arrayPointer.abstractedArrayPointer(arraySize)

    for (i in 0 until array.size) {
        array[i] = actionPointer.value.toDouble()
    }

    return true
}

@CName(externName = "testFun", shortName = "testFun")
fun testFun(): Int = i++

@CName(externName = "close", shortName = "close")
fun close() {
    Log.close()
}


private var _Impl: MT4Client? = null
private fun Impl(): MT4Client = _Impl ?: throwException("setTestIndex(int) not called")


@CName(externName = "setTestIndex", shortName = "setTestIndex")
fun setTestIndex(index: Int) {
    Log.io("setTestIndex(index)")
    _Impl = runCatching { Strategy.getVPEA(index) }.getOrLogException()
}

@CName(externName = "onNewBar", shortName = "onNewBar")
fun onNewBar() {
    Log.io("onNewBar")
    runCatching { Impl().onNewBar() }.getOrLogException()
}

@CName(externName = "onStart", shortName = "onStart")
fun onStart(symbol: Int, pipPrice: Double) {
    Log.io("onStart $symbol $pipPrice")
    runCatching { Impl().onStart(symbol, pipPrice) }.getOrLogException()
}

@CName(externName = "goToActionMode", shortName = "goToActionMode")
fun goToActionMode() {
    Log.io("goToActionMode")
    runCatching {
        Impl().goToActionMode()
    }.getOrLogException()
}

@CName(externName = "request", shortName = "request")
fun request(actionPointer: CPointer<IntVar>, arrayPointer: CArrayPointer<DoubleVar>, arraySize: Int): Boolean {
    val abstractedActionPointer = actionPointer.abstractedVarPointer()
    val abstractedArrayPointer = arrayPointer.abstractedArrayPointer(arraySize)
    Log.io("Request-IN action=$abstractedActionPointer $abstractedArrayPointer")
    val out = runCatching { Impl().request(abstractedActionPointer, abstractedArrayPointer) }.getOrLogException()
    Log.io("Request-OUT action=$abstractedActionPointer $abstractedArrayPointer")
    return out
}


@CName(externName = "response", shortName = "response")
fun response(actionPointer: CPointer<IntVar>, arrayPointer: CArrayPointer<DoubleVar>, arraySize: Int) {
    val abstractedActionPointer = actionPointer.abstractedVarPointer()
    val abstractedArrayPointer = arrayPointer.abstractedArrayPointer(arraySize)
    Log.io("Response-IN action=$abstractedActionPointer $abstractedArrayPointer")
    runCatching {
        Impl().response(abstractedActionPointer, abstractedArrayPointer)
    }.getOrLogException()
    Log.io("Response-OUT action=$abstractedActionPointer $abstractedArrayPointer")
}

