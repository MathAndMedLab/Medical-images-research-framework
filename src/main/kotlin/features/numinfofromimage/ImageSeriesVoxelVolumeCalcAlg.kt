package features.numinfofromimage

import core.algorithm.Algorithm
import core.array.map2d
import core.common.toBicolor
import core.data.ParametrizedData
import core.data.attribute.MirfAttributes
import core.data.medimage.ImageSeries
import core.log.MirfLogFactory
import features.dicomimage.data.DicomAttributes
import org.slf4j.Logger

/**
 * Calculates the volume of the given [ImageSeries].
 */
class ImageSeriesVoxelVolumeCalcAlg : Algorithm<ImageSeries, ParametrizedData<*>> {

    override fun execute(input: ImageSeries): ParametrizedData<*> {

        var result = 0.0

        for (image in input.images) {

            if (!image.attributes.hasAttribute(MirfAttributes.THRESHOLDED))
                log.warn("Warning, performing volume calculation on non-thresholded image, possible unexpected result")

            val onePixelVolume = image.attributes.getAttributeValue(DicomAttributes.ONE_PIXEL_VOLUME_MM3)

            val img = image.image
            val volumes = img!!.toBicolor().map2d { if (it) onePixelVolume else 0.0 }

            result += volumes.sum()

        }
        return ParametrizedData(result)
    }
}
