package com.bugsnag.example.kotlinmp

import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer


class MockPointer<T>(override var value: T) : AbstractedPointer<T>
class MockArrayPointer<T>(val list: MutableList<T>) : AbstractedArrayPointer<T> {
    override val size: Int
        get() = list.size

    override fun get(index: Int): T = list[index]

    override fun set(index: Int, value: T) {
        this.list[index] = value
    }
}

inline val <T> T.p get() = MockPointer(this)
inline val <T> MutableList<T>.p get() = MockArrayPointer(this)

