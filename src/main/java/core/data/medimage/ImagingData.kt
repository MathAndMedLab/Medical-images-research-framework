package core.data.medimage

interface ImagingData<I> {

    fun getImage(): I
    fun getImageDataAsShortArray(): ShortArray
    fun getImageDataAsByteArray(): ShortArray
    fun getImageDataAsIntArray(): ShortArray

    fun copy(): ImagingData<I>
    fun applyMask(mask: Array<ByteArray>)

    val width: Int
    val height: Int
}