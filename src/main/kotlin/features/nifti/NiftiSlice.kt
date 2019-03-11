package features.nifti

import core.data.medimage.MedImage
import ij.process.ImageProcessor
import java.awt.image.BufferedImage

class NiftiSlice(private val processor: ImageProcessor) : MedImage() {

    override val image: BufferedImage
        get() = processor.bufferedImage

    override val extension: String
        get() = "NII"

    override fun clone(): MedImage {
        log.error("Nifti slice performs shallow copy for now. BEWARE")
        return NiftiSlice(processor.clone() as ImageProcessor)
    }
}
