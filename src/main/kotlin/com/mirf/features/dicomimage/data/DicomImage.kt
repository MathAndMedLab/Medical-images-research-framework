package com.mirf.features.dicomimage.data

import com.pixelmed.dicom.AttributeList
import com.mirf.core.data.medimage.MedImage
import com.mirf.core.data.medimage.RawImageData
import java.awt.image.BufferedImage

/**
 * Dummy implementation of dicom image
 */
//TODO: (avlomakin) move to com.mirf.core.data
class DicomImage : MedImage {

    override val attributes: DicomAttributeCollection

    private var _image: BufferedImage? = null
    private var _imageAttributesVersion = -1
    override val image: BufferedImage
        get() {
            if (_imageAttributesVersion != attributes.pixelDataAttributesVersion) {
                log.info("attributes has been updated, recreating image")
                _image = attributes.buildHumanReadableImage()
                _imageAttributesVersion = attributes.pixelDataAttributesVersion
            }

            if (_image == null) {
                log.warn("no image is presented, but versions are equal. Forcing image generation")
                _image = attributes.buildHumanReadableImage()
            }

            if (_image == null)
                throw MirfDicomException("failed to generate human readableImage")

            return _image!!
        }

    // TODO(musatian): make this function instead of image to assit both desktop and Android support
    override val rawImage: RawImageData
        get() = TODO("not implemented")

    override val extension: String
        get() = "DICOM"

    constructor(attributes: DicomAttributeCollection) : super() {
        this.attributes = attributes
    }

    override fun clone(): DicomImage {
        return DicomImage(attributes.clone())
    }


    companion object {
        /**
         * Creates dicom image with no attributes
         */
        fun createEmpty(): DicomImage {
            return DicomImage(DicomAttributeCollection(AttributeList()))
        }
    }
}
