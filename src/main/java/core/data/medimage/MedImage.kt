package core.data.medimage

import core.data.AttributeCollection
import core.data.Data

import java.awt.image.BufferedImage

/**
 * Data about medical image, including both pixels data and metadata.
 *
 *
 * Any particular type of medical image should extend MedImage
 */
abstract class MedImage : Data, Cloneable {

    abstract val image: BufferedImage

    /**
     * Retrieves file extension of MedImage
     * @return extension
     */
    abstract val extension: String

    protected constructor()

    protected constructor(attributes: AttributeCollection) : super(attributes)

    public abstract override fun clone(): MedImage
}
