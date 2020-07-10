package com.bugsnag.example.kotlinmp.api.server

import com.bugsnag.example.kotlinmp.api.client.*
import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer

enum class MT4Action {
    CLOSE,
    GET_CLOSE_PRICE,
    GET_INDICATOR_VALUE,
    GET_INDICATOR_NUM_PARAMS,
    OPEN_POSITION,
    CLOSE_POSITION,
}

interface MT4Wrapper {

    fun onTick(actionPointer: AbstractedPointer<Int>,
               arrayPointer: AbstractedArrayPointer<Double>)

    fun onStart(actionPointer: AbstractedPointer<Int>,
                arrayPointer: AbstractedArrayPointer<Double>)

}

// TODO Create a JVM onStart function + on tick loop
/*
 val repository: PriceRepository where does this go ?

There needs to be a layer between the EA and MT4Wrapper that is responsible for
converting data structure and abstract away the weird exchanges that have to be done
due to the limitation of using a DLL

- This could be MT4WrapperImpl
- This could be a class that an action set whenever a function is called. You can use coroutines for blocking (Channel)
 */

class MT4WrapperImpl() : MT4Wrapper {

    override fun onTick(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {
        actionPointer.value = arrayPointer.size
        arrayPointer.forEachIndexed { index, value ->
            arrayPointer[index] = value + 1
        }
    }

    override fun onStart(actionPointer: AbstractedPointer<Int>, arrayPointer: AbstractedArrayPointer<Double>) {

    }

}