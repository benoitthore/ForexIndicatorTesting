package com.bugsnag.example.kotlinmp.utils

interface AbstractedPointer<T> {
    var value: T
}

interface AbstractedArrayPointer<T> : Iterable<T> {
    val size: Int
    operator fun get(index: Int): T
    operator fun set(index: Int, value: T)
    override fun iterator(): Iterator<T> = object : Iterator<T> {
        var i = 0
        override fun hasNext(): Boolean = i < size

        override fun next(): T = get(i++)

    }

    fun copy(): Iterable<T> = toList()
}