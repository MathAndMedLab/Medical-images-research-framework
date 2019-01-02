package features.numinfofromimage

import core.algorithm.Algorithm
import core.data.ParametrizedData
import core.data.attribute.MirfAttributes
import core.data.medimage.ImageSeriesData
import features.dicomimage.data.DicomAttributes

/**
 * Calculates the volume of the given [ImageSeriesData].
 */
class ImageSeriesVoxelVolumeCalcAlg : Algorithm<ImageSeriesData, ParametrizedData<*>> {

    override fun execute(input: ImageSeriesData): ParametrizedData<*> {

        var result = 0.0

        for (image in input.images) {
            //TODO: (avlomakin) replace with Log.Warning
            if (!image.attributes.hasAttribute(MirfAttributes.THRESHOLDED))
                println("Warning, performing volume calculation on non-thresholded image, possible unexpected result")

            val onePixelVolume = image.attributes.findAttributeValue(DicomAttributes.ONE_PIXEL_VOLUME)!!

            val img = image.image
            val raw = img!!.raster.getPixels(img.minX, img.minY, img.width, img.height, null as IntArray?)

            for (aLine in raw)
                result += if (aLine != 0) onePixelVolume else 0.0

        }
        return ParametrizedData(result)
    }
}
