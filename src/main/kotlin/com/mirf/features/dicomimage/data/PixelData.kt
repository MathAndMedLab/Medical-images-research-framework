package com.mirf.features.dicomimage.data

import com.pixelmed.dicom.DicomOutputStream
import com.pixelmed.dicom.TagFromName
import java.io.File
import java.io.FileOutputStream
import javax.imageio.stream.FileImageInputStream

/**
 * PixelData dicom
 */
class PixelData(private var dicomAttributeCollection: DicomAttributeCollection, private val fileName : String) {

    /**
     * Read image and write to byte array image with preamble
     */
    fun writeToFile() {
        val pixelData = dicomAttributeCollection.getAttributePixelData()
        val outputStream = FileOutputStream(File(fileName))
        val transferSyntaxUID = dicomAttributeCollection.getAttributeValue(TagFromName.TransactionUID)
        val dicomOutputStream = DicomOutputStream(outputStream, "", transferSyntaxUID)
        pixelData.write(dicomOutputStream)
    }

    /**
     * Get ByteArray pixelData
     */
    fun getByteArray(): ByteArray {
        val file = File(fileName)
        val imageInputStream = FileImageInputStream(file)
        val dirtyByteArrayPixelData = ByteArray(file.length().toInt())
        imageInputStream.read(dirtyByteArrayPixelData)
        imageInputStream.close()
        return convertDirtyByteArrayToCleanByteArray(dirtyByteArrayPixelData)
    }

    /**
     * Convert byte array image with preamble to image byte array image
     */
    private fun convertDirtyByteArrayToCleanByteArray(dirtyByteArrayPixelData: ByteArray): ByteArray  {
        val cleanByteArrayPixelData: ByteArray = ByteArray(dirtyByteArrayPixelData.size - 144)
        for (i in cleanByteArrayPixelData.indices) {
            cleanByteArrayPixelData[i] = dirtyByteArrayPixelData[i + 144]
        }
        return cleanByteArrayPixelData
    }

}