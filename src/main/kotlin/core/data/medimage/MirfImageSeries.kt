package core.data.medimage

import core.data.AttributeCollection
import core.data.MirfData

/**
 * ImageSeries stores a list of [MedImage]
 */
class MirfImageSeries(override val images: List<MedImage>, attributes: AttributeCollection = AttributeCollection()) : MirfData(attributes), ImageSeries
