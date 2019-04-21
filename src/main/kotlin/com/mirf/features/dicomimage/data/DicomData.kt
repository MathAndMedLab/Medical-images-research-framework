package com.mirf.features.dicomimage.data

import com.mirf.core.array.BooleanArray2D
import com.mirf.core.data.medimage.ImagingData
import com.pixelmed.dicom.DicomOutputStream
import com.pixelmed.dicom.TagFromName
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import javax.imageio.stream.FileImageInputStream

class DicomData : ImagingData<BufferedImage> {
    private var dicomAttributeCollection: DicomAttributeCollection? = null
    private var bitsAllocated: Int = 0
    private val byteArray: ByteArray? = null
    private val shortArray: ShortArray? = null
    private val intArray: IntArray? = null
    private var dirtyByteArrayPixelData: ByteArray? = null
    private var cleanByteArrayPixelData: ByteArray? = null


    constructor(dicomAttributeCollection: DicomAttributeCollection) {
        this.dicomAttributeCollection = dicomAttributeCollection
        bitsAllocated = Integer.parseInt(dicomAttributeCollection.getAttributeValue(TagFromName.BitsAllocated))
        analisePixelData()
    }

    private fun analisePixelData() {
        pixelDataWriteToFile()
        readPixelDataFileAndWriteToDirtyByteArray()
        convertDirtyByteArrayToCleanByteArray()




    }

    private fun convertDirtyByteArrayToCleanByteArray() {
        cleanByteArrayPixelData = ByteArray(dirtyByteArrayPixelData!!.size - 144)
        for (i in cleanByteArrayPixelData!!.indices) {
            cleanByteArrayPixelData!![i] = dirtyByteArrayPixelData?.get(i + 144)!!
        }
    }

    private fun readPixelDataFileAndWriteToDirtyByteArray() {
        val file = File("pixeldata.txt")
        val imageInputStream = FileImageInputStream(file)
        dirtyByteArrayPixelData = ByteArray(file.length().toInt())
        imageInputStream.read(dirtyByteArrayPixelData)
        imageInputStream.close()
    }

    private fun pixelDataWriteToFile() {
        val pixelData = dicomAttributeCollection?.getAttributePixelData()
        val outputStream = FileOutputStream(File("src/test/resources/pixeldata.txt"))
        val transferSyntaxUID = dicomAttributeCollection?.getAttributeValue(TagFromName.TransactionUID)
        val dicomOutputStream = DicomOutputStream(outputStream, "", transferSyntaxUID)
        pixelData?.write(dicomOutputStream)
    }

    override fun getImage(): BufferedImage {
        return dicomAttributeCollection!!.buildHumanReadableImage()
    }

    override fun getImageDataAsShortArray(): ShortArray {
        return shortArray!!
    }

    override fun getImageDataAsByteArray(): ByteArray {
        return byteArray!!
    }

    override fun getImageDataAsIntArray(): IntArray {
        return intArray!!
    }

    override fun copy(): ImagingData<BufferedImage> {
        return DicomData(dicomAttributeCollection!!.clone())
    }

    override fun applyMask(mask: BooleanArray2D) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val width: Int
        get() = dicomAttributeCollection!!.buildHumanReadableImage().width
    override val height: Int
        get() = dicomAttributeCollection!!.buildHumanReadableImage().height
}