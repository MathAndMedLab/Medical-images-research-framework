package features.numinfofromimage

import core.algorithm.Algorithm
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

    private val log : Logger = MirfLogFactory.currentLogger

    override fun execute(input: ImageSeries): ParametrizedData<*> {

        var result = 0.0

        for (image in input.images) {

            if (!image.attributes.hasAttribute(MirfAttributes.THRESHOLDED))
                log.warn("Warning, performing volume calculation on non-thresholded image, possible unexpected result")

            val onePixelVolume = image.attributes.getAttributeValue(DicomAttributes.ONE_PIXEL_VOLUME)

            val img = image.image
            val raw = img.raster.getPixels(img.minX, img.minY, img.width, img.height, null as IntArray?)

            for (aLine in raw)
                result += if (aLine != 0) onePixelVolume else 0.0

        }
        return ParametrizedData(result)
    }
}
