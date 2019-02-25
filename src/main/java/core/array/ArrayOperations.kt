package core.array

import core.data.MirfException
import kotlin.experimental.and


internal object ConcurrentEnvironment{
    val THREAD_NUM = 4
}

/**
 * Elementwise multiplies array by [other]. Note that any overflow will be truncated
 */
fun ByteArray.multiplyElementwise(other: ByteArray) {

    if(this.size != other.size)
        throw MirfException("elementwise multiplication failed: wrong size (${this.size} vs ${other.size}) ")

    for (i in 0 until this.size){
        this[i] = (this[i] * other[i]).toByte()
    }
}

fun ShortArray.expandToByteArray(): ByteArray{
    fun getBytes(value: Short): List<Byte>{
        val low = value.and(0xff).toByte()
        val high = value.toInt().shr(8).and(0xff).toByte()
        return listOf(high, low)
    }

    return this.flatMap { x -> getBytes(x)}.toByteArray()
}

fun ByteArray.composeValuesToShortArray(): ShortArray{
    val result = ShortArray(this.size / 2)
    for(i in 0 until this.size step 2){
        val value = this[i].toInt().shl(8) + this[i]
        result[i / 2] = value.toShort()
    }
    return result
}