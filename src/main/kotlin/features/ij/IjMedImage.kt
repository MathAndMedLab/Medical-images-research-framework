package features.ij

import core.data.medimage.MedImage
import ij.process.ImageProcessor
import java.awt.image.BufferedImage

class IjMedImage(private val processor: ImageProcessor, override val extension: String) : MedImage() {

    override val image: BufferedImage
        get() = processor.bufferedImage

    override fun clone(): MedImage {
        log.error("Nifti slice performs shallow copy for now. BEWARE")
        return IjMedImage(processor.clone() as ImageProcessor, extension)
    }
}