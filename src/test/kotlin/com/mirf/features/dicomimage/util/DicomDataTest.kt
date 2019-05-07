package com.mirf.features.dicomimage.util
import com.mirf.features.dicomimage.data.DicomAttributeCollection
import com.mirf.features.dicomimage.data.DicomData
import com.pixelmed.dicom.DicomException
import com.pixelmed.dicom.TagFromName
import org.junit.Assert
import org.junit.Test

class DicomDataTest {

    /**
     * Test dicom with 8 bits allocated
     */
    @Test
    fun testDicomData_vs_result_PixelMed_8BitsAllocated() {
        val dicomInputFile = "src/test/resources/dicomDataTest/8bitsAllocatedExample.DCM"
        val list = DicomReader.readDicomImageAttributesFromLocalFile(dicomInputFile)
        val dicomAttributeCollection : DicomAttributeCollection = DicomAttributeCollection(list)

        val dicomData : DicomData = DicomData(dicomAttributeCollection)

        val byteArrayPixelMed: ByteArray = list.pixelData.byteValues
        val byteArrayDicomData: ByteArray = dicomData.getImageDataAsByteArray()

        Assert.assertEquals(byteArrayPixelMed.size, byteArrayDicomData.size)
        Assert.assertArrayEquals(byteArrayPixelMed, byteArrayDicomData)


        var thrown = false
        try {
            list.pixelData.shortValues
        } catch (e : DicomException) {
            thrown = true
        }

        Assert.assertTrue(thrown)

        Assert.assertNotNull(dicomData.getImageDataAsShortArray())
        Assert.assertNotNull(dicomData.getImageDataAsIntArray())
    }

    /**
     * Test dicom with 16 bits allocated
     */
    @Test
    fun testDicomData_vs_result_PixelMed_16BitsAllocated() {
        val dicomInputFile = "src/test/resources/dicomDataTest/16bitsAllocatedExample.dcm"
        val list = DicomReader.readDicomImageAttributesFromLocalFile(dicomInputFile)
        val dicomAttributeCollection : DicomAttributeCollection = DicomAttributeCollection(list)

        val dicomData : DicomData = DicomData(dicomAttributeCollection)

        val shortArrayPixelMed: ShortArray = list.pixelData.shortValues
        val shortArrayDicomData: ShortArray = dicomData.getImageDataAsShortArray()

        Assert.assertEquals(shortArrayPixelMed.size,  shortArrayDicomData.size)
        Assert.assertArrayEquals(shortArrayPixelMed,shortArrayDicomData)
        var thrown = false
        try {
            list.pixelData.byteValues
        } catch (e : DicomException) {
            thrown = true
        }

        Assert.assertTrue(thrown)
        Assert.assertNotNull(dicomData.getImageDataAsByteArray())
        Assert.assertNotNull(dicomData.getImageDataAsIntArray())
    }

}