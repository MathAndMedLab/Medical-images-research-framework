package com.mirf.core.data.medimage

import com.mirf.core.data.AttributeCollection
import com.mirf.core.data.MirfData

import java.awt.image.BufferedImage

/**
 * Data about medical image, including both pixels data and metadata.
 *
 *
 * Any particular type of medical image should extend MedImage
 */
open class MedImage : MirfData, Cloneable {

    open val rawImage: RawImageData? = null

    // Migrate all the code to use RawImageData
    // make this property not abstract because it is blocking android development
    open val image: BufferedImage? = null

    /**
     * Retrieves file extension of MedImage
     * @return extension
     */
    open val extension: String = ""

    protected constructor()

    protected constructor(attributes: AttributeCollection) : super(attributes)

    public open override fun clone(): MedImage {return MedImage(attributes.clone())
    }
}
