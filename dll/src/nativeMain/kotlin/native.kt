package example

import com.bugsnag.example.kotlinmp.api.server.MT4WrapperImpl
import com.bugsnag.example.kotlinmp.platformId
import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.DoubleVar
import kotlinx.cinterop.IntVar

@CName(externName = "testFun", shortName = "testFun")
fun createThing(): Int = platformId()

val wrapper = MT4WrapperImpl()

@CName(externName = "on_start", shortName = "on_start")
fun onStart(action: CPointer<IntVar>, buffer: CArrayPointer<DoubleVar>, bufferSize: Int) {
    wrapper.onStart(action.abstracted(), buffer.abstracted(bufferSize))
}

@CName(externName = "on_tick", shortName = "on_tick")
fun onTick(action: CPointer<IntVar>, buffer: CArrayPointer<DoubleVar>, bufferSize: Int) {
    wrapper.onTick(action.abstracted(), buffer.abstracted(bufferSize))
}
