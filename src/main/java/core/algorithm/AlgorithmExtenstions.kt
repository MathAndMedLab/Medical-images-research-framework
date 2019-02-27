package core.algorithm

import core.data.medimage.ImageSeries
import core.data.medimage.MedImage

fun Algorithm<in MedImage,out MedImage>.asImageSeriesAlg(createNew: Boolean = false) : Algorithm<ImageSeries, ImageSeries>{
    return MedImageEditorPropagator(this, createNew)
}

class MedImageEditorPropagator(
        private val imageAlg: Algorithm<in MedImage,out MedImage>,
        private val createNew: Boolean = false) : Algorithm<ImageSeries, ImageSeries>{

    override fun execute(input: ImageSeries): ImageSeries {
        val newImages = input.images.map{ imageAlg.execute(it) }

        if(createNew){
            val attributes = input.attributes.clone()
            return ImageSeries(newImages, attributes)
        }

        input.images = newImages
        return  input
    }
}