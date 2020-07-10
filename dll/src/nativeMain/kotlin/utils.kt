package example

import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer
import kotlinx.cinterop.*


fun CPointer<DoubleVar>.abstracted() =
        object : AbstractedPointer<Double> {
            override var value: Double
                get() = this@abstracted.pointed.value
                set(value) {
                    this@abstracted.pointed.value = value
                }
        }

fun CPointer<IntVar>.abstracted() =
        object : AbstractedPointer<Int> {
            override var value: Int
                get() = this@abstracted.pointed.value
                set(value) {
                    this@abstracted.pointed.value = value
                }
        }

fun CArrayPointer<DoubleVar>.abstracted(size: Int) =
        object : AbstractedArrayPointer<Double> {
            override val size: Int
                get() = size

            override fun get(index: Int): Double = get(index)

            override fun set(index: Int, value: Double) {
                this@abstracted.set(index, value)
            }

        }

fun CArrayPointer<IntVar>.abstracted(size: Int) =
        object : AbstractedArrayPointer<Int> {
            override val size: Int
                get() = size

            override fun get(index: Int): Int = get(index)

            override fun set(index: Int, value: Int) {
                this@abstracted.set(index, value)
            }
        }
