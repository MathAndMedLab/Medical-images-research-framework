package com.mirf.playground

import com.mirf.core.algorithm.Algorithm
import com.mirf.core.array.BooleanArray2D
import com.mirf.core.data.attribute.MirfAttributes
import com.mirf.core.data.medimage.MedImage
import kotlin.math.sqrt

class AddCircleMaskAlg : Algorithm<MedImage, MedImage> {

    override fun execute(input: MedImage): MedImage {
        val mask = createCircleMask(input.image!!.width, input.image!!.height)
        input.attributes.add(MirfAttributes.IMAGE_SEGMENTATION_MASK, mask)
        return input
    }

    private fun createCircleMask(width: Int, height: Int): BooleanArray2D {
        val result = BooleanArray2D.create(height, width)

        val coef = 1 / 3f

        val a: Float = (height / 2f) * coef
        val b: Float = (width / 2f) * coef

        val centerX = width / 2
        val centerY = height / 2
        //(x/b) ^ 2 + (y/a)^2 = 1 - ellipse

        for (i in 0 until a.toInt()) {
            val y = a - i
            val x = sqrt((1 - (y / a) * (y / a)) * b * b)
            val from = ((centerX - x).toInt())
            val to = ((centerX + x).toInt())
            result[centerY - y.toInt()].fill(true, from, to)
            result[centerY + y.toInt() - 1].fill(true, from, to)
        }

        //log.info("\n${result.flatMap { x -> listOf(*x.map{ "$it" }.toTypedArray(), "\n" )}}")

        return result
    }
}