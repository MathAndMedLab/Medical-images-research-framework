package com.mirf.features.tensorflow

import java.awt.image.BufferedImage

interface ImageProcessor {
    fun process(image: BufferedImage) : BufferedImage
}

