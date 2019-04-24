package com.mirf.core.data.medimage

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO



class BufferedImageRawImage : RawImageData {
    private val _image: BufferedImage

    constructor(image : BufferedImage): super() {
        this._image = image
    }

    override fun getHeight(): Int {
        return _image.getHeight()
    }

    override fun getWidth(): Int {
        return _image.getWidth()
    }

    override fun getImage(): ByteArray? {
        val baos = ByteArrayOutputStream()
        ImageIO.write(this._image, "jpg", baos);
        return baos.toByteArray()
    }
}