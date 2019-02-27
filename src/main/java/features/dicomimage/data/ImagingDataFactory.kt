package features.dicomimage.data

import core.data.medimage.ImagingData
import core.data.medimage.RawPixelToRgbTransformer
import core.data.medimage.ShortImagingData
import java.awt.image.BufferedImage

object ImagingDataFactory{

    fun create(pixelData: ShortArray, rows: Int, columns: Int): ImagingData<BufferedImage> {
        val pixels = Array(rows) {ShortArray(columns)}

        for(i in 0 until (rows * columns) step columns){
            pixels[i / columns] = pixelData.sliceArray(i until (i+columns))
        }
        return ShortImagingData(pixels, RawPixelToRgbTransformer())
    }
}