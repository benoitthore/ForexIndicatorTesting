package example

import com.bugsnag.example.kotlinmp.utils.AbstractedArrayPointer
import com.bugsnag.example.kotlinmp.utils.AbstractedPointer
import kotlinx.cinterop.*


fun CPointer<DoubleVar>.abstractedVarPointer() =
        object : AbstractedPointer<Double> {
            override var value: Double
                get() = this@abstractedVarPointer.pointed.value
                set(value) {
                    this@abstractedVarPointer.pointed.value = value
                }

            override fun toString(): String {
                return "Pointer($value)"
            }
        }

fun CPointer<IntVar>.abstractedVarPointer() =
        object : AbstractedPointer<Int> {
            override var value: Int
                get() = this@abstractedVarPointer.pointed.value
                set(value) {
                    this@abstractedVarPointer.pointed.value = value
                }

            override fun toString(): String {
                return "Pointer($value)"
            }
        }

fun CArrayPointer<DoubleVar>.abstractedArrayPointer(size: Int) =
        object : AbstractedArrayPointer<Double> {
            override val size: Int
                get() = size

            // Old code
            // override fun get(index: Int): Double = get(index)

            // fix
            override fun get(index: Int): Double = this@abstractedArrayPointer.get(index)

            override fun set(index: Int, value: Double) {
                this@abstractedArrayPointer.set(index, value)
            }

            override fun toString(): String {
                return "ArrayPointer(${iterator().asSequence().toList()})"
            }

        }

fun CArrayPointer<IntVar>.abstractedArrayPointer(size: Int) =
        object : AbstractedArrayPointer<Int> {
            override val size: Int
                get() = size

            override fun get(index: Int): Int = this@abstractedArrayPointer.get(index)

            override fun set(index: Int, value: Int) {
                this@abstractedArrayPointer.set(index, value)
            }

            override fun toString(): String {
                return "ArrayPointer(${iterator().asSequence().toList()})"
            }
        }
