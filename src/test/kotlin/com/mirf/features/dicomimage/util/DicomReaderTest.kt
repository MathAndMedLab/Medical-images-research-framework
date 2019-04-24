package com.mirf.features.dicomimage.util

import com.pixelmed.dicom.Attribute
import com.pixelmed.dicom.AttributeList
import com.pixelmed.dicom.AttributeTag
import com.pixelmed.dicom.TagFromName
import org.junit.Assert
import org.junit.Test

class DicomReaderTest {

    @Test
    fun readDicomImageAttributesFromLocalFile_localDicom_readsWithoutErrors() {
        val dicomInputFile = "src/test/resources/exampleDicom.dcm"
        val list = DicomReader.readDicomImageAttributesFromLocalFile(dicomInputFile)

        // Assert that tags are in correspondence with what is expected from file
        Assert.assertEquals("1.2.840.10008.1.2.1", getTagInformation(list, TagFromName.TransferSyntaxUID))
        Assert.assertEquals("1", getTagInformation(list, TagFromName.SamplesPerPixel))
        Assert.assertTrue(list.get(TagFromName.PixelData) != null)
    }

    @Test
    fun readDicomImagePixelDataFromLocalFile_localDicom_readsWithoutErrors() {
        val dicomInputFile = "src/test/resources/exampleDicom.dcm"
        val bufferedImages = DicomReader.readDicomImagePixelDataFromLocalFile(dicomInputFile)

        // Just check the size of images array
        Assert.assertEquals(16, bufferedImages.size.toLong())
    }

    private fun getTagInformation(list: AttributeList, attrTag: AttributeTag): String {
        return Attribute.getDelimitedStringValuesOrEmptyString(list, attrTag)
    }
}