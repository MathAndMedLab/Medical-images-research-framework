package com.mirf.features.numinfofromimage

import com.mirf.core.algorithm.Algorithm
import com.mirf.core.array.map2d
import com.mirf.core.common.toBicolor
import com.mirf.core.data.ParametrizedData
import com.mirf.core.data.attribute.MirfAttributes
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.features.dicomimage.data.DicomAttributes

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
