package core.common

import java.awt.image.BufferedImage
import java.awt.image.ColorConvertOp


fun BufferedImage.logSize(): String = "${this.height} x ${this.width}"

fun BufferedImage.convertColorspace(newType: Int): BufferedImage {
    val result = BufferedImage(
            this.width,
            this.height,
            newType)
    result.graphics.drawImage(this, 0, 0, null)
    return result
}