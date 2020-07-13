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


@CName(externName = "testArray", shortName = "testArray")
fun testArray(actionPointer: CPointer<IntVar>, arrayPointer: CArrayPointer<DoubleVar>, arraySize : Int): Boolean {
      actionPointer.abstractedVarPointer().value = 42
      val array = arrayPointer.abstractedArrayPointer(arraySize)
      for(i in 0 until array.size){
            array[i] = arrayPointer.abstractedVarPointer().value
      }
      return true
}
/*

@CName(externName = "testFun", shortName = "testFun")
fun testFun(): Int = i++


@CName(externName = "testFun", shortName = "testFun")
fun onNewBar() =
        Impl.onNewBar()

@CName(externName = "goToActionMode", shortName = "goToActionMode")
fun goToActionMode() =
        Impl.goToActionMode()

@CName(externName = "request", shortName = "request")
fun request(actionPointer: CPointer<IntVar>, arrayPointer: CArrayPointer<DoubleVar>, arraySize: Int): Boolean {
    return Impl.request(actionPointer.abstractedVarPointer(), arrayPointer.abstractedArrayPointer(arraySize))
}


@CName(externName = "response", shortName = "response")
fun response(actionPointer: CPointer<IntVar>, arrayPointer: CArrayPointer<DoubleVar>, arraySize: Int) =
        Impl.response(actionPointer.abstractedVarPointer(), arrayPointer.abstractedArrayPointer(arraySize))


private val Impl = Strategy.get(getTestEA())



*/