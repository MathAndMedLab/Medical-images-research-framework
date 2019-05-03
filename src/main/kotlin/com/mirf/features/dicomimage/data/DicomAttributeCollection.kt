package com.mirf.features.dicomimage.data

import com.pixelmed.dicom.AttributeList
import com.pixelmed.display.ConsumerFormatImageMaker
import com.mirf.core.data.AttributeCollection
import com.mirf.core.data.attribute.DataAttribute
import com.mirf.features.dicomimage.copy
import com.pixelmed.dicom.Attribute
import com.pixelmed.dicom.AttributeTag
import java.awt.image.BufferedImage

class DicomAttributeCollection : AttributeCollection {

    private val dicomAttributes: AttributeList

    var dicomAttributesVersion = 0
        private set

    var mirfAttributesVersion = 0
        private set

    /**
     * Version of the newest attribute, related to pixelData
     */
    val pixelDataAttributesVersion = dicomAttributesVersion

    override val version
        get() = maxOf(dicomAttributesVersion, mirfAttributesVersion)

    constructor(dicomAttributes: AttributeList, mirfAttributes: Collection<DataAttribute<*>> = ArrayList()) : super(mirfAttributes) {
        this.dicomAttributes = dicomAttributes
    }

    fun buildHumanReadableImage(): BufferedImage {
        updateDicomAttrByMirfAttr()
        return ConsumerFormatImageMaker.makeEightBitImage(dicomAttributes)
    }

    /**
     * Creates deep copy of the [DicomAttributeCollection]
     */
    override fun clone(): DicomAttributeCollection {
        val clonedAttributes = attributes.map { it.copy() }
        val clonedDicomAttributes = dicomAttributes.copy()
        return DicomAttributeCollection(clonedDicomAttributes, clonedAttributes)
    }

    /**
    Checks if internal [attributes] contains newer version of DICOM related attributes, and updates [dicomAttributes] if so.
    If Attribute is missing from the [dicomAttributes], but presented in [attributes], it will be ADDED too
     */
    private fun updateDicomAttrByMirfAttr() {
        attributes.forEach {
            if (MirfPixelmedAttributeMapper.canMap(it.tag)) {
                MirfPixelmedAttributeMapper.syncPixelmedAttributes(dicomAttributes, it)
            }
        }
    }

    /**
     * Checks if internal [attributes] contains newer version of DICOM related attributes, and updates [dicomAttributes] if so.
     * If Attribute is missing from the [attributes], but presented in [dicomAttributes], it will be SKIPPED
     */
    private fun updateMirfAttrByMirfAttr() {
        //TODO:(avlomakin)
    }

    private var cacheRequestedDicomAttributesInMirfCollection: Boolean = true


    /**
     * Get value attribute
     */
    fun getAttributeValue(attrTag: AttributeTag): String {
        return Attribute.getDelimitedStringValuesOrEmptyString(dicomAttributes, attrTag)
    }

    /**
     * Get attribute dicom pixel data
     */
    fun getAttributePixelData(): Attribute {
        return dicomAttributes.pixelData
    }

    override fun find(attributeTag: String): DataAttribute<*>? {
        val mirfAttr = attributes.firstOrNull { x -> x.tag == attributeTag }

        if (mirfAttr == null) {

            log.info("Attribute with '$attributeTag' tag is not presented as mirf attribute. Check if possible to create from pixelmed attributes")

            if (!MirfPixelmedAttributeMapper.canMap(attributeTag)) {
                log.info("no pixelmed analogue for attribute with '$attributeTag' tag")
                return null
            }

            //pixelmed attributeList has no find, so here is try-catch based find
            return try {
                val createdAttr = MirfPixelmedAttributeMapper.CreateMirfAttribute(attributeTag, dicomAttributes)

                if (cacheRequestedDicomAttributesInMirfCollection) {
                    attributes.add(createdAttr)
                }

                createdAttr
            } catch (e: Exception) {
                log.error(e.message)
                null
            }
        }

        return mirfAttr
    }
}
