package com.mirf.features.imagefilters

import com.mirf.core.data.MirfException
import java.awt.Image
import java.awt.image.BufferedImage


fun BufferedImage.resize(newWidth: Int, newHeight: Int): BufferedImage {
    val tmp = this.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)
    val dimg = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB)

    val g2d = dimg.createGraphics()
    g2d.drawImage(tmp, 0, 0, null)
    g2d.dispose()

    return dimg
}

fun BufferedImage.applyMask(mask: BufferedImage) {

    if(this.width != mask.width || this.height != mask.height)
        throw MirfException("failed to apply mask: inconsistent size (${this.width}, ${this.height}) vs (${mask.width}, ${mask.height})")

    for (i in 0..this.height){
        for(j in 0..this.width)
            if(mask.getRGB(i, j) == 0)
                this.setRGB(i, j, 0)
    }
}
