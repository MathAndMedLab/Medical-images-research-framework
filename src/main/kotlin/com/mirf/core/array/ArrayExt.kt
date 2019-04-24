package com.mirf.core.array

import com.mirf.core.data.MirfException
import kotlin.experimental.and


internal object ConcurrentEnvironment {
    val THREAD_NUM = 4
}

/**
 * Elementwise multiplies array by [other]. Note that any overflow will be truncated
 */
fun ByteArray.multiplyElementwise(other: ByteArray) {

    if (this.size != other.size)
        throw MirfException("elementwise multiplication failed: wrong size (${this.size} vs ${other.size}) ")

    for (i in 0 until this.size) {
        this[i] = (this[i] * other[i]).toByte()
    }
}

fun ShortArray.expandToByteArray(): ByteArray {
    fun getBytes(value: Short): List<Byte> {
        val low = value.and(0xff).toByte()
        val high = value.toInt().shr(8).and(0xff).toByte()
        return listOf(high, low)
    }

    return this.flatMap { x -> getBytes(x) }.toByteArray()
}

fun ByteArray.composeValuesToShortArray(): ShortArray {
    val result = ShortArray(this.size / 2)
    for (i in 0 until this.size step 2) {
        val value = this[i].toInt().shl(8) + this[i]
        result[i / 2] = value.toShort()
    }
    return result
}

fun Array<ShortArray>.to1D(): ShortArray {
    return this.flatMap { x -> x.toList() }.toShortArray()
}

fun Array<ByteArray>.to1D(): ByteArray {
    return this.flatMap { x -> x.toList() }.toByteArray()
}

fun Array<IntArray>.to1D(): IntArray {
    return this.flatMap { x -> x.toList() }.toIntArray()
}

fun Array<BooleanArray>.to1D(): BooleanArray {
    return this.flatMap { x -> x.toList() }.toBooleanArray()
}

fun Array<ByteArray>.deepCopy(): Array<ByteArray> {
    return this.clone().map { y -> y.clone() }.toTypedArray()
}

fun Array<IntArray>.deepCopy(): Array<IntArray> {
    return this.clone().map { y -> y.clone() }.toTypedArray()
}

fun Array<BooleanArray>.deepCopy(): Array<BooleanArray> {
    return this.clone().map { y -> y.clone() }.toTypedArray()
}

fun Array<ShortArray>.deepCopy(): Array<ShortArray> {
    return this.clone().map { y -> y.clone() }.toTypedArray()
}

fun <R> BooleanArray2D.map2d(transform: (Boolean) -> R): List<R> {
    val result = mutableListOf<R>()
    for (i in 0 until this.rows) {
        result.addAll(this[i].map(transform))
    }
    return result
}
fun <R> ByteArray2D.map2d(transform: (Byte) -> R): List<R> {
    val result = mutableListOf<R>()
    for (i in 0 until this.rows) {
        result.addAll(this[i].map(transform))
    }
    return result
}
fun <R> ShortArray2D.map2d(transform: (Short) -> R): List<R> {
    val result = mutableListOf<R>()
    for (i in 0 until this.rows) {
        result.addAll(this[i].map(transform))
    }
    return result
}
fun <R> IntArray2D.map2d(transform: (Int) -> R): List<R> {
    val result = mutableListOf<R>()
    for (i in 0 until this.rows) {
        result.addAll(this[i].map(transform))
    }
    return result
}

fun BooleanArray2D.logSize(): String = "${this.rows} x ${this.columns}"
fun ByteArray2D.logSize(): String = "${this.rows} x ${this.columns}"
fun ShortArray2D.logSize(): String = "${this.rows} x ${this.columns}"
fun IntArray2D.logSize(): String = "${this.rows} x ${this.columns}"

