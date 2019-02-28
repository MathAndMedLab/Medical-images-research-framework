package features.dicomimage.data

import core.array.ShortArray2D
import core.data.medimage.ImagingData
import core.data.medimage.RawPixelToRgbTransformer
import core.data.medimage.ShortImagingData
import java.awt.image.BufferedImage

object ImagingDataFactory{

    fun create(pixelData: ShortArray, rows: Int, columns: Int): ImagingData<BufferedImage> {

        val pixels = ShortArray2D.create(rows, columns, pixelData)
        return ShortImagingData(pixels, RawPixelToRgbTransformer())
    }
}