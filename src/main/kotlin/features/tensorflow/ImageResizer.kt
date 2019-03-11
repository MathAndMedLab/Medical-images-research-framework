package features.tensorflow

import features.imagefilters.resize
import java.awt.image.BufferedImage

class ImageResizer(private val targetWidthInPixels: Int, private val targetHeightInPixels: Int) : ImageProcessor {
    override fun process(image: BufferedImage): BufferedImage {
        return image.resize(targetWidthInPixels, targetHeightInPixels)
    }
}