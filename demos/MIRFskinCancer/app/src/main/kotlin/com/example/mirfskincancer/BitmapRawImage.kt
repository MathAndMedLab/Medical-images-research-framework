package com.example.mirfskincancer

import android.graphics.Bitmap
import core.data.medimage.RawImageData
import java.io.ByteArrayOutputStream
import android.R.array
import android.opengl.ETC1.getHeight
import java.nio.ByteBuffer
import android.graphics.Color
import core.data.Data
import core.data.AttributeCollection


// TODO: make here medImage and clean up the code
class BitmapRawImage : RawImageData, Data {
    override val attributes: AttributeCollection = AttributeCollection()
    private val _image: Bitmap?

    constructor(image: Bitmap?) : super() {
        this._image = image
    }

    override fun getHeight(): Int {
        return _image!!.getHeight()
    }

    override fun getWidth(): Int {
        return _image!!.getWidth()
    }

    override fun getImage(): ByteArray? {
        val size = getWidth() * getHeight()
        val buffer = ByteBuffer.allocate(size)
        _image!!.copyPixelsToBuffer(buffer)
        return buffer.array()
    }

    fun getFloatImageArray(resizedHeight: Int, resizedWidth: Int): FloatArray {
        val resized = Bitmap.createScaledBitmap(this._image, resizedWidth, resizedHeight, true)
        val channel = 3
        val bitmapSize = resizedWidth * resizedHeight
        var rIndex = 0
        var gIndex = 0
        var bIndex = 0
        var buff = FloatArray(channel * bitmapSize)
        var bitBuff = IntArray(bitmapSize)
        var k = 0;
        resized!!.getPixels(bitBuff, 0, resizedWidth, 0, 0, resizedWidth, resizedHeight);
        for (i in 0..resizedHeight - 1) {
            for (j in 0..resizedWidth - 1) {
                if (k >= bitmapSize - 1) {
                    break
                }
                rIndex = k * 3
                gIndex = rIndex + 1
                bIndex = gIndex + 2

                var color = bitBuff[k]
                buff[rIndex] = (Color.red(color)).toFloat()
                buff[gIndex] = (Color.green(color)).toFloat()
                buff[bIndex] = (Color.blue(color)).toFloat()
                k++
            }
        }

        return buff;
    }
}