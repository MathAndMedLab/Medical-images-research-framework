package com.mirf.features.dicomimage

import com.mirf.core.data.attribute.AttributeCreationException
import com.mirf.core.data.attribute.DataAttribute

/**
 * Manages DICOM attributes creation
 */
object DicomAttributeCreator {

    /**
     * Creates DICOM data attribute
     * @param tag attribute tag  - (XXXX,XXXX) with hexadecimal numbers
     * @param name attribute name
     * @param value attribute value
     * @return created attribute for Dicom format
     */
    @Throws(AttributeCreationException::class)
    fun createDicomAttribute(tag: String, name: String, value: Any): DataAttribute<*> {
        if (!isDicomTag(tag))
            throw AttributeCreationException("invalid tag shape: (XXXX, XXXX) required")

        return DataAttribute(name, tag, value)
    }


    private fun isDicomTag(tag: String?): Boolean {

        if (tag == null || tag.isEmpty())
            return false

        val pattern = "^[(][0-9a-fA-F]{4}[,][0-9a-fA-F]{4}[)]$"
        return tag.matches(pattern.toRegex())
    }

}
