package com.mirf.features.numinfofromimage

import com.mirf.core.array.BooleanArray2D
import com.mirf.core.array.map2d
import com.mirf.core.common.VolumeValue
import com.mirf.core.common.sum
import com.mirf.core.common.toBicolor
import com.mirf.core.data.MirfException
import com.mirf.core.data.attribute.MirfAttributes
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.data.medimage.MedImage

fun ImageSeries.getVolume(): VolumeValue {

    val atomicVolume = this.attributes[MirfAttributes.ATOMIC_ELEMENT_VOLUME]

    if (!this.isThresholded)
        throw MirfException("Volume calculation can only be applied to the thresholded series")

    var result = VolumeValue.zero

    for (image in this.images) {
        val mask = image.toBicolor()
        val allPixelVolumes = mask.map2d { if (it) atomicVolume else VolumeValue.zero }
        result += allPixelVolumes.sum()
    }
    return result
}

private fun MedImage.toBicolor(): BooleanArray2D {
    return this.image!!.toBicolor()
}

private val ImageSeries.isThresholded: Boolean
    get() = this.attributes.hasAttribute(MirfAttributes.THRESHOLDED)

