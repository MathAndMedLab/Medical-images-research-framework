package core.data.medimage

import core.array.BooleanArray2D

interface ImagingData<I> {

    fun getImage(): I
    fun getImageDataAsShortArray(): ShortArray
    fun getImageDataAsByteArray(): ShortArray
    fun getImageDataAsIntArray(): ShortArray

    fun copy(): ImagingData<I>
    fun applyMask(mask: BooleanArray2D)

    val width: Int
    val height: Int
}