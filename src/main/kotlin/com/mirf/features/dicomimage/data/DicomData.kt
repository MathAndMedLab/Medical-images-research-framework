package com.mirf.features.dicomimage.data

import com.mirf.core.array.BooleanArray2D
import com.mirf.core.data.medimage.ImagingData
import com.pixelmed.dicom.TagFromName
import java.awt.image.BufferedImage

class DicomData : ImagingData<BufferedImage> {
    private var dicomAttributeCollection: DicomAttributeCollection? = null
    private var bitsAllocated: Int = 0
    private val byteArray: ByteArray? = null
    private val shortArray: ShortArray? = null
    private val intArray: IntArray? = null


    constructor(dicomAttributeCollection: DicomAttributeCollection) {
        this.dicomAttributeCollection = dicomAttributeCollection
        bitsAllocated = Integer.parseInt(dicomAttributeCollection.getAttributeValue(TagFromName.BitsAllocated))
        analisePixelData()

    }

    override fun getImage(): BufferedImage {
        return dicomAttributeCollection.buildHumanReadableImage()
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