package com.mirf.core.data.medimage

import com.mirf.core.data.MirfException

@ExperimentalUnsignedTypes
class IntermediateImageRepresentation private constructor(val data: Array<UIntArray>,
                                                          val bitsPerPixel : Int){

    fun getRawData(): ByteArray{

        if(bitsPerPixel == 1)
            throw MirfException("$bitsPerPixel bit(s) per pixel is not currently supported")

        return data.flatMap { x -> x.flatMap { y -> getBytes(y).toList() } }.toByteArray()
    }

    private fun getBytes(value: UInt): ByteArray {

        val bytesPerPixel = bitsPerPixel / 8
        val result = ByteArray(bytesPerPixel)
        val mask = 0xFFu
        var temp = value

        for(i in 0 until result.size){
            result[i] = temp.and(mask).toByte()
            temp = temp.shr(8)
        }
        result.reverse()

        return result
    }


    companion object {
        fun createFromRawData(rawData: ByteArray, width: Int, height: Int,  bitsPerPixel: Int) : IntermediateImageRepresentation {
            if(bitsPerPixel > 32 || bitsPerPixel % 8 != 0)
                throw IllegalArgumentException("$bitsPerPixel is invalid value for bits per pixel")

            val formattedData = Array(height) { UIntArray(width)}
            val bytesPerPixel = (bitsPerPixel / 8)
            val i = 0
            while (i < rawData.size){
                val bytes = rawData.slice(i..i+bytesPerPixel).toByteArray()
                formattedData[i / (height * bytesPerPixel)][i % (height * bytesPerPixel)] = bytes.toUInt()
            }

            return IntermediateImageRepresentation(formattedData, bitsPerPixel)
        }
    }
}

@ExperimentalUnsignedTypes
private fun ByteArray.toUInt(): UInt {
    if(this.size > 4)
        throw IllegalArgumentException()
    var result = 0u

    for(i in 0 until this.size){
        result = result.or(this[i].toUInt().shl(i * 8))
    }
    return result
}
