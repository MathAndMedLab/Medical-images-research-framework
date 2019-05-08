package com.mirf.core.data.medimage

import com.mirf.core.array.BooleanArray2D

interface ImagingData<I> {

    fun getImage(): I
    fun getImageDataAsShortArray(): ShortArray
    fun getImageDataAsByteArray(): ByteArray
    fun getImageDataAsIntArray(): IntArray

    fun copy(): ImagingData<I>
    fun applyMask(mask: BooleanArray2D)

    val width: Int
    val height: Int
}