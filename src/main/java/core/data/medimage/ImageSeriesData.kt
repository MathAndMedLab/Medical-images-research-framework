package core.data.medimage

import core.data.AttributeCollection
import core.data.Data

/**
 * ImageSeries stores a list of [MedImage]
 */
class ImageSeriesData(var images: List<MedImage>, attributes: AttributeCollection = AttributeCollection()) : Data(attributes)
