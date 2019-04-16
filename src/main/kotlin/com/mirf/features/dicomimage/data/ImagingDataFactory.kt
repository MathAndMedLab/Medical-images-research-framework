package com.mirf.features.dicomimage.data

import com.mirf.core.array.ShortArray2D
import com.mirf.core.data.medimage.ImagingData
import com.mirf.core.data.medimage.RawPixelToRgbTransformer
import com.mirf.core.data.medimage.ShortImagingData
import java.awt.image.BufferedImage

object ImagingDataFactory{

    fun create(pixelData: ShortArray, rows: Int, columns: Int): ImagingData<BufferedImage> {

        val pixels = ShortArray2D.create(rows, columns, pixelData)
        return ShortImagingData(pixels, RawPixelToRgbTransformer())
    }
}