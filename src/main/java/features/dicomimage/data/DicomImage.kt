package features.dicomimage.data


import core.data.AttributeCollection
import core.data.medimage.MedImage

import java.awt.image.BufferedImage

/**
 * Dummy implementation of dicom image
 */
//TODO: (avlomakin) move to core.data
class DicomImage : MedImage {

    override var image: BufferedImage? = null

    override val extension: String
        get() = "DICOM"

    constructor(pixelData: BufferedImage?) {
        this.image = pixelData
    }

    private constructor(attributes: AttributeCollection, pixelData: BufferedImage?) : super(attributes) {
        this.image = pixelData
    }

    override fun clone(): DicomImage {
        return DicomImage(attributes.clone(), image)
    }
}
