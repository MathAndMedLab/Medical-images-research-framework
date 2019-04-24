package com.mirf.features.dicomimage.data

import com.pixelmed.dicom.*
import com.mirf.core.data.attribute.DataAttribute
import com.mirf.core.data.attribute.DataAttributeMockup
import com.mirf.core.data.attribute.MirfAttributes
import com.mirf.core.data.medimage.ImagingData
import java.awt.image.BufferedImage

object MirfPixelmedAttributeMapper {

    private val supportedMirfAttributes: HashSet<DataAttributeMockup<*>> = hashSetOf(MirfAttributes.IMAGING_DATA)

    fun canMap(mockup: DataAttributeMockup<*>) = supportedMirfAttributes.contains(mockup)
    fun canMap(attribute: DataAttribute<*>) = canMap(attribute.tag)
    fun canMap(attributeTag: String) = supportedMirfAttributes.any { it.tag == attributeTag }

    fun createImagingData(pixelmedAttributes: AttributeList): ImagingData<BufferedImage> {
        val rows = pixelmedAttributes.get(TagFromName.Rows)
        val columns = pixelmedAttributes.get(TagFromName.Columns)
        val rawPixels = pixelmedAttributes.get(TagFromName.PixelData)
        return ImagingDataFactory.create(rawPixels.shortValues, rows.getSingleIntegerValueOrDefault(0), columns.getSingleIntegerValueOrDefault(0))
    }

    private fun syncImagingData(attributes: AttributeList, mirfAttribute: DataAttribute<ImagingData<BufferedImage>>) {
        val pixelData = OtherWordAttribute(TagFromName.PixelData)
        pixelData.setValues(mirfAttribute.value.getImageDataAsShortArray())
        attributes.put(pixelData)

        val rows = UnsignedShortAttribute(TagFromName.Rows)
        rows.setValue(mirfAttribute.value.height.toShort())
        attributes.put(rows)

        val columns = UnsignedShortAttribute(TagFromName.Columns)
        columns.setValue(mirfAttribute.value.width.toShort())
        attributes.put(columns)
    }

    fun CreateMirfAttribute(mirfAttrTag: String, attributes: AttributeList): DataAttribute<*> {
        return when (mirfAttrTag) {
            MirfAttributes.IMAGING_DATA.tag -> MirfAttributes.IMAGING_DATA.new(createImagingData(attributes))
            else -> throw MirfDicomException("Failed to create mirf attribute: No transform method")
        }
    }

    /**
     * Updates all [Attribute] related to [mirfAttribute] in [attributes]
     */
    fun syncPixelmedAttributes(attributes: AttributeList, mirfAttribute: DataAttribute<*>) {
        when (mirfAttribute.tag) {
            MirfAttributes.IMAGING_DATA.tag -> syncImagingData(attributes, mirfAttribute as DataAttribute<ImagingData<BufferedImage>>)
        }
    }
}