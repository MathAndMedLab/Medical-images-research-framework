package com.mirf.features.dicomimage

import com.pixelmed.dicom.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


/**
 * Creates deep copy of the given [AttributeList]
 */
fun AttributeList.copy() : AttributeList{

    val coreStream = ByteArrayOutputStream()
    val outputStream = DicomOutputStream(BinaryOutputStream(coreStream, false), null, "1.2.840.10008.1.2")
    this.write(outputStream)

    val arr = coreStream.toByteArray()

    val copy = AttributeList()
    copy.read(DicomInputStream(ByteArrayInputStream(arr)))

    return copy
}