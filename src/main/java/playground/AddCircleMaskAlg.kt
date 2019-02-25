package playground

import core.algorithm.Algorithm
import core.data.attribute.MirfAttributes
import core.data.medimage.MedImage
import core.log.MirfLogFactory
import org.slf4j.Logger
import kotlin.math.sqrt

class AddCircleMaskAlg : Algorithm<MedImage, MedImage> {

    private val log : Logger = MirfLogFactory.currentLogger

    override fun execute(input: MedImage): MedImage {
        val mask = createCircleMask(input.image.width, input.image.height)
        input.attributes.add(MirfAttributes.IMAGE_SEGMENTATION_MASK, mask)
        return input
    }

    private fun createCircleMask(width: Int, height: Int): Array<ByteArray> {
        val result = Array(height) { ByteArray(width  * 2)}

        val a : Float = (height / 2f)
        val b : Float = (width / 2f)

        //(x/b) ^ 2 + (y/a)^2 = 1 - ellipse

        for(i in 0 until height / 2){
            val y = a - i
            val x = sqrt((1 - (y / a) * (y / a)) * b * b)
            val from = 2 * ((b - x).toInt())
            val to = 2 * ((x+b).toInt())
            result[i].fill(1,from ,to)
            result[height - i - 1].fill(1, from,to)
        }

        //log.info("\n${result.flatMap { x -> listOf(*x.map{ "$it" }.toTypedArray(), "\n" )}}")

        return result
    }
}