package core.algorithm

import core.data.medimage.ImageSeriesData
import core.data.medimage.MedImage

fun Algorithm<in MedImage,out MedImage>.asImageSeriesAlg(createNew: Boolean = false) : Algorithm<ImageSeriesData, ImageSeriesData>{
    return MedImageEditorPropagator(this, createNew)
}

class MedImageEditorPropagator(
        private val imageAlg: Algorithm<in MedImage,out MedImage>,
        private val createNew: Boolean = false) : Algorithm<ImageSeriesData, ImageSeriesData>{

    override fun execute(input: ImageSeriesData): ImageSeriesData {
        val newImages = input.images.map{ imageAlg.execute(it) }

        if(createNew){
            val attributes = input.attributes.clone()
            return ImageSeriesData(newImages, attributes)
        }

        input.images = newImages
        return  input
    }
}