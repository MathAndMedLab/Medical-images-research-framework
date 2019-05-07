package com.mirf.features.dicomimage.data

import com.mirf.core.array.BooleanArray2D

import com.mirf.core.data.medimage.ImagingData
import com.pixelmed.dicom.DicomOutputStream
import com.pixelmed.dicom.TagFromName
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.imageio.stream.FileImageInputStream

/**
 * Realize ImageData on dicom
 */
class DicomData(private var dicomAttributeCollection: DicomAttributeCollection) : ImagingData<BufferedImage> {

    private var bitsAllocated: Int = 0
    private lateinit var byteArray: ByteArray
    private lateinit var shortArray: ShortArray
    private lateinit var intArray: IntArray
    private lateinit var cleanByteArrayPixelData: ByteArray


    init {
        bitsAllocated = Integer.parseInt(dicomAttributeCollection.getAttributeValue(TagFromName.BitsAllocated))
        analisePixelData()
    }

    /**
     * Analise image and initialized byteArray, shortArray and IntArray
     */
    private fun analisePixelData() {
        val pixelData : PixelData = PixelData(dicomAttributeCollection,"src/main/resources/pixeldata.txt")
        pixelData.writeToFile()
        cleanByteArrayPixelData = pixelData.getByteArray()

        when (bitsAllocated) {
            8 -> {
                byteArray = cleanByteArrayPixelData
                shortArray = byteArrayToShortArray(byteArray)
                intArray = byteArrayToIntArray(byteArray)
            }
            16 -> {
                shortArray = cleanByteArrayPixelDataToShortArray(cleanByteArrayPixelData)
                byteArray = shortArrayToByteArray(shortArray, dicomAttributeCollection)
                intArray = shortArrayToIntArray(shortArray)
            }
            32 -> intArray = cleanByteArrayPixelDataToIntArray(cleanByteArrayPixelData)
        }
    }

    /**
     * Get dicom image in the view BufferedImage
     */
    override fun getImage(): BufferedImage {
        return dicomAttributeCollection.buildHumanReadableImage()
    }

    /**
     * Get dicom image in the view ShortArray
     */
    override fun getImageDataAsShortArray(): ShortArray {
        if(bitsAllocated == 32)
            throw DicomDataException("Can't get shortArray because bits allocated = 32")
        return shortArray
    }

    /**
     * Get dicom image in the view ByteArray
     */
    override fun getImageDataAsByteArray(): ByteArray {
        if(bitsAllocated == 32)
            throw DicomDataException("Can't get byteArray because bits allocated = 32")
        return byteArray
    }

    /**
     * Get dicom image in the view IntArray
     */
    override fun getImageDataAsIntArray(): IntArray {
        return intArray
    }

    override fun copy(): ImagingData<BufferedImage> {
        return DicomData(dicomAttributeCollection.clone())
    }

    override fun applyMask(mask: BooleanArray2D) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val width: Int
        get() = dicomAttributeCollection.buildHumanReadableImage().width
    override val height: Int
        get() = dicomAttributeCollection.buildHumanReadableImage().height
}
