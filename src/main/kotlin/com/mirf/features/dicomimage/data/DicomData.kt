package com.mirf.features.dicomimage.data

import com.mirf.core.array.BooleanArray2D
import com.mirf.core.data.medimage.ImagingData
import java.awt.image.BufferedImage

class DicomData : ImagingData<BufferedImage> {


    override fun getImage(): BufferedImage {
        return dicomAttributeList.buildHumanReadableImage()
    }

    override fun getImageDataAsShortArray(): ShortArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getImageDataAsByteArray(): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getImageDataAsIntArray(): IntArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun copy(): ImagingData<BufferedImage> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun applyMask(mask: BooleanArray2D) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val width: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val height: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}