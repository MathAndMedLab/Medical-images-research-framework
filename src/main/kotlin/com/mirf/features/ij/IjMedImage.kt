package com.mirf.features.ij

import com.mirf.core.data.medimage.MedImage
import com.mirf.core.data.medimage.RawImageData
import ij.process.ImageProcessor
import java.awt.image.BufferedImage

class IjMedImage(private val processor: ImageProcessor, override val extension: String) : MedImage() {

    override val image: BufferedImage
        get() = processor.bufferedImage

    override val rawImage: RawImageData
        get() = TODO("not implemented")

    override fun clone(): MedImage {
        log.error("Nifti slice performs shallow copy for now. BEWARE")
        return IjMedImage(processor.clone() as ImageProcessor, extension)
    }
}