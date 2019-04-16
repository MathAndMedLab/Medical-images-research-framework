package com.mirf.features.dicomimage.util

import com.pixelmed.dicom.AttributeList
import com.pixelmed.dicom.DicomInputStream
import com.pixelmed.display.ConsumerFormatImageMaker
import com.mirf.features.dicomimage.data.DicomAttributeCollection
import com.mirf.features.dicomimage.data.DicomImage

import java.awt.image.BufferedImage
import java.io.InputStream
import java.util.ArrayList
import java.util.Arrays

/**
 * Holds different methods for reading Dicom images.
 */
object DicomReader {

    // TODO(sabrinamusatian): migrate from pixelmed.dicom.AttributeList to MedImageAttribute

    /**
     * Reads Dicom image attributes from a given local path.
     *
     * @param dicomInputFile a path to the dicom image on disk.
     * @return list with dicom attributes;
     */
    fun readDicomImageAttributesFromLocalFile(dicomInputFile: String): AttributeList {
        val dicomAttributes = AttributeList()
        try {
            dicomAttributes.read(dicomInputFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return dicomAttributes
    }

    fun readDicomImage(input: InputStream): DicomImage {
        val dicomStream = DicomInputStream(input)
        val dicomAttributes = AttributeList()

        dicomAttributes.read(dicomStream)

        return DicomImage(DicomAttributeCollection(dicomAttributes))
    }

    /**
     * Reads only pixel data of a Dicom image from a given [AttributeList].
     *
     * @param attributeList a list of already read dicom attributes.
     * @return list with pixel data, e.g. list of slices in dicom image.
     */
    fun readDicomImagePixelDataFromAttributeList(attributeList: AttributeList): List<BufferedImage> {
        var pixelData = ArrayList<BufferedImage>()
        try {
            val imgs = ConsumerFormatImageMaker.makeEightBitImages(attributeList)
            pixelData = ArrayList(Arrays.asList(*imgs))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return pixelData
    }

    /**
     * Reads only pixel data of a Dicom image from a from a given local path.
     *
     * @param dicomInputFile a path to the dicom image on disk.
     * @return list with pixel data, e.g. list of slices in dicom image.
     */
    fun readDicomImagePixelDataFromLocalFile(dicomInputFile: String): List<BufferedImage> {
        val attributeList = readDicomImageAttributesFromLocalFile(dicomInputFile)
        return readDicomImagePixelDataFromAttributeList(attributeList)
    }

}
