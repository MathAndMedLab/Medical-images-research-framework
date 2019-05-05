package com.mirf.core.data.medimage

import com.mirf.core.array.logSize
import com.mirf.core.common.convertColorspace
import com.mirf.core.common.debugDisplayInWindow
import com.mirf.core.common.logSize
import com.mirf.core.data.MirfException
import com.mirf.core.data.attribute.MirfAttributes
import java.awt.Color
import java.awt.image.BufferedImage

fun MedImage.getImageWithHighlightedSegmentation(highlightColor: Color = Color(0x00, 0xff, 0x00, 128)): BufferedImage {

    if (!this.attributes.hasAttribute(MirfAttributes.IMAGE_SEGMENTATION_MASK))
        throw MirfException("[Segmentation highlighter] image has no segmentation mask")

    val bitMask = this.attributes[MirfAttributes.IMAGE_SEGMENTATION_MASK]

    val image = this.image!!.convertColorspace(BufferedImage.TYPE_INT_RGB)

    if (bitMask.rows != image.height || bitMask.columns != image.width)
        throw MirfException("[Segmentation highlighter] image and segmentation mask have different size: " +
                "(${image.logSize()}) vs (${bitMask.logSize()}) ")

    val mask = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)

    for (i in 0 until mask.width)
        for (j in 0 until mask.height)
            if (bitMask[j][i])
                mask.setRGB(i, j, highlightColor.rgb)

    image.graphics.drawImage(mask, 0, 0, null)

    return image
}