package features.ij

import core.data.MirfData
import core.data.medimage.ImageSeries
import core.data.medimage.MedImage
import ij.ImagePlus

class IjImageSeries(private val image: ImagePlus) : MirfData(), ImageSeries {

    val extension: String = image.fileInfo.fileFormatString

    private val _images = lazy { createImages() }

    private fun createImages(): List<MedImage> {
        return List(image.stackSize) { x -> IjMedImage(image.stack.getProcessor(x + 1), extension) }
    }

    override val images: List<MedImage>
        get() = _images.value

}