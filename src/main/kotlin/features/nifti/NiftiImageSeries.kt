package features.nifti

import core.data.MirfData
import core.data.medimage.ImageSeries
import core.data.medimage.MedImage
import ij.ImageStack

class NiftiImageSeries(val imageStack: ImageStack) : MirfData(), ImageSeries {

    private val _images = lazy { createImages() }

    private fun createImages(): List<MedImage> {
        return List(imageStack.size) { x -> NiftiSlice(imageStack.getProcessor(x + 1)) }
    }

    override val images: List<MedImage>
        get() = _images.value
}