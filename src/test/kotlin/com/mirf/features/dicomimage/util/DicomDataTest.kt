package com.mirf.features.dicomimage.util
import com.mirf.features.dicomimage.data.DicomAttributeCollection
import com.mirf.features.dicomimage.data.DicomData
import com.pixelmed.dicom.TagFromName
import org.junit.Assert
import org.junit.Test


class DicomDataTest {
    @Test
    fun testDicomData_vs_result_PixelMed_8BitsAllocated() {
        val dicomInputFile = "src/test/resources/0002.DCM"
        val list = DicomReader.readDicomImageAttributesFromLocalFile(dicomInputFile)
        val dicomAttributeCollection : DicomAttributeCollection = DicomAttributeCollection(list)

        val dicomData : DicomData = DicomData(dicomAttributeCollection)

        var byteArrayPixelMed: ByteArray = list.pixelData.byteValues
        var byteArrayDicomData: ByteArray = dicomData.getImageDataAsByteArray()

        Assert.assertEquals(byteArrayPixelMed.size, byteArrayDicomData.size)
        Assert.assertArrayEquals(byteArrayPixelMed, byteArrayDicomData)


        var thrown = false
        try {
            list.pixelData.shortValues
        } catch (e : Exception) {
            thrown = true
        }

        Assert.assertTrue(thrown)

        Assert.assertNotNull(dicomData.getImageDataAsShortArray())
        Assert.assertNotNull(dicomData.getImageDataAsIntArray())
    }

    @Test
    fun testDicomData_vs_result_PixelMed_16BitsAllocated() {
        val dicomInputFile = "src/test/resources/000001.dcm"
        val list = DicomReader.readDicomImageAttributesFromLocalFile(dicomInputFile)
        val dicomAttributeCollection : DicomAttributeCollection = DicomAttributeCollection(list)

        val dicomData : DicomData = DicomData(dicomAttributeCollection)

        var shortArrayPixelMed: ShortArray = list.pixelData.shortValues
        var shortArrayDicomData: ShortArray = dicomData.getImageDataAsShortArray()

        Assert.assertEquals(shortArrayPixelMed.size,  shortArrayDicomData.size)
        Assert.assertArrayEquals(shortArrayPixelMed,shortArrayDicomData)
        var thrown = false
        try {
            list.pixelData.byteValues
        } catch (e : Exception) {
            thrown = true
        }
        Assert.assertNotNull(dicomData.getImageDataAsByteArray())
    }

}