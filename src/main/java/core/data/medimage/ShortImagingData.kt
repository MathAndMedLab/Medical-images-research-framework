package core.data.medimage

import core.array.deepCopy
import core.array.to1D
import core.data.MirfException
import java.awt.image.BufferedImage

class ShortImagingData(rawPixels: Array<ShortArray>, rawPixelTransformer: RawPixelToRgbTransformer): ImagingData<BufferedImage> {

    override fun applyMask(mask: Array<ByteArray>) {
        if()
    }

    val rawPixels: Array<ShortArray>
    val rawPixelTransformer : RawPixelToRgbTransformer
    override val width: Int
    override val height: Int

    init {
        if(!isPixelsValid(rawPixels))
            throw MirfException("failed to create image data: array is in invalid state")

        this.rawPixels = rawPixels
        this.width = rawPixels[0].size
        this.height = rawPixels.size
        this.rawPixelTransformer = rawPixelTransformer
    }

    override fun copy(): ImagingData<BufferedImage> {
        return ShortImagingData(rawPixels.deepCopy(), rawPixelTransformer.copy())
    }

    private fun isPixelsValid(pixels: Array<ShortArray>) : Boolean {
        return pixels.maxBy{x -> x.size}?.size == pixels.minBy { x -> x.size }?.size
    }

    override fun getImage(): BufferedImage {
        TODO("not implemented")
    }

    override fun getImageDataAsShortArray(): ShortArray {
        return rawPixels.to1D()
    }

    override fun getImageDataAsByteArray(): ShortArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getImageDataAsIntArray(): ShortArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}