package features.imagefilters

import core.algorithm.Algorithm
import core.data.attribute.MirfAttributes
import core.data.attribute.Switch
import core.data.medimage.ImageSeriesData
import core.data.medimage.MedImage

/**
 * Filters the pixels of all images provided in [ImageSeriesData] to be within specified boundaries.
 */
class ImageSeriesThresholdAlg(private val lowerBound: Byte, private val upperBound: Byte) : Algorithm<ImageSeriesData, ImageSeriesData> {

    override fun execute(input: ImageSeriesData): ImageSeriesData {
        input.images.parallelStream().forEach{ this.thresholdImage(it) }
        return input
    }

    private fun thresholdImage(medImage: MedImage): MedImage {

        //TODO: (avlomakin) add MedImage cloning if required

        val img = medImage.image

        val raw = img!!.raster.getPixels(img.minX, img.minY, img.width, img.height, null as IntArray?)
        for (i in raw.indices)
            raw[i] = if (raw[i] in lowerBound..upperBound) raw[i] else 0

        img.raster.setPixels(img.minX, img.minY, img.width, img.height, raw)

        medImage.attributes.add(MirfAttributes.THRESHOLDED, Switch.get())

        return medImage
    }
}
