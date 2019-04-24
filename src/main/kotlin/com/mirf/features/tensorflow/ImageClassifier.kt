package com.mirf.features.tensorflow

import java.awt.image.BufferedImage

//TODO:(avlomakin) add another classification result types
class ImageClassifier(val model: Model, val imageProcessor: ImageProcessor? = null) {

    fun run(input : List<BufferedImage>) : BinaryClassificationResult {

        val  images = input.map{x -> imageProcessor?.process(x) ?: x }
        //if image processor is set, process images
        //get ByteArray from images
        //pass to model
        //parse result

        return BinaryClassificationResult((1..input.size).map { it % 2 == 0 }.toBooleanArray())
    }
}