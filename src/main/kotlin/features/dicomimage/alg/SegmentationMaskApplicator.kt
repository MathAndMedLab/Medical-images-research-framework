package features.dicomimage.alg

import core.algorithm.Algorithm
import core.algorithm.AlgorithmException
import core.array.multiplyElementwise
import core.data.MirfException
import core.data.attribute.MirfAttributes
import core.data.medimage.MedImage

class SegmentationMaskApplicator(
        private val applicationMode: ImageTransformMode)
    : Algorithm<MedImage, MedImage> {

    override fun execute(input: MedImage): MedImage {

        if (!input.attributes.hasAttribute(MirfAttributes.IMAGE_SEGMENTATION_MASK))
            throw MirfException("Failed to apply mask: no segmentation mask is presented")

        val mask = input.attributes[MirfAttributes.IMAGE_SEGMENTATION_MASK]

        return when (applicationMode) {
            ImageTransformMode.Update -> {
                input.attributes[MirfAttributes.IMAGING_DATA].applyMask(mask)
                input
            }
            ImageTransformMode.GenerateNew -> {
                val result = input.clone()
                result.attributes[MirfAttributes.IMAGING_DATA].applyMask(mask)

                return result
            }
            else -> throw AlgorithmException("unknown image transform mode: $applicationMode")
        }
    }
}






