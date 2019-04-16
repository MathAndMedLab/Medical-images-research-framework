package com.mirf.core.algorithm

import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.data.medimage.MedImage
import com.mirf.core.data.medimage.MirfImageSeries

fun Algorithm<in MedImage, out MedImage>.asImageSeriesAlg(): Algorithm<ImageSeries, ImageSeries> {
    return MedImageEditorPropagator(this)
}

class MedImageEditorPropagator(
        private val imageAlg: Algorithm<in MedImage, out MedImage>) : Algorithm<ImageSeries, ImageSeries> {

    override fun execute(input: ImageSeries): ImageSeries {
        val newImages = input.images.map { imageAlg.execute(it) }

        val attributes = input.attributes.clone()
        return MirfImageSeries(newImages, attributes)
    }
}