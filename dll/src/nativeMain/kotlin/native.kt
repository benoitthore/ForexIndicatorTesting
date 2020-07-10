package example

import com.bugsnag.example.kotlinmp.platformId
import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.DoubleVar
import kotlinx.cinterop.IntVar


var i = 0

@CName(externName = "testFun", shortName = "testFun")
fun createThing(): Int = i++

//val wrapper = MT4WrapperImpl()
//
//@CName(externName = "on_start", shortName = "on_start")
//fun onStart(action: CPointer<IntVar>, buffer: CArrayPointer<DoubleVar>, bufferSize: Int) {
//    wrapper.onStart(action.abstracted(), buffer.abstracted(bufferSize))
//}
//
//@CName(externName = "on_new_candle")
//fun onNewCandle() {
//    wrapper.onNewCandle()
//}
