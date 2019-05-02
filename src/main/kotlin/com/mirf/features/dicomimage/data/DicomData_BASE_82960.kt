package com.mirf.features.dicomimage.data

import com.mirf.core.array.BooleanArray2D
import com.mirf.core.data.medimage.ImagingData
import com.pixelmed.dicom.DicomOutputStream
import com.pixelmed.dicom.TagFromName
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.imageio.stream.FileImageInputStream

class DicomData : ImagingData<BufferedImage> {
    private var dicomAttributeCollection: DicomAttributeCollection? = null
    private var bitsAllocated: Int = 0
    private var byteArray: ByteArray? = null
    private var shortArray: ShortArray? = null
    private var intArray: IntArray? = null
    private var cleanByteArrayPixelData: ByteArray? = null


    constructor(dicomAttributeCollection: DicomAttributeCollection) {
        this.dicomAttributeCollection = dicomAttributeCollection
        bitsAllocated = Integer.parseInt(dicomAttributeCollection.getAttributeValue(TagFromName.BitsAllocated))
        analisePixelData()
    }

    private fun analisePixelData() {
        pixelDataWriteToFile()
        var dirtyByteArrayPixelData: ByteArray? = readPixelDataFileAndWriteToDirtyByteArray()
        convertDirtyByteArrayToCleanByteArray(dirtyByteArrayPixelData!!)

        if (bitsAllocated == 8) {
            byteArray = cleanByteArrayPixelData
            shortArray = byteArrayToShortArray(byteArray!!)
            intArray = byteArrayToIntArray(byteArray!!)
        }

        else if (bitsAllocated == 16) {
            shortArray = cleanByteArrayPixelDataToShortArray(cleanByteArrayPixelData!!)
            byteArray = shortArrayToByteArray(shortArray!!)
            intArray = shortArrayToIntArrat(shortArray!!)
        }

        else if (bitsAllocated == 32) {
            intArray = cleanByteArrayPixelDataToIntArray(cleanByteArrayPixelData!!)
            byteArray = intArrayToByteArray(intArray!!)
            shortArray = intArrayToShortArray(intArray!!)
        }
    }

    private fun intArrayToShortArray(intArray: IntArray): ShortArray? {
        return null
    }

    private fun intArrayToByteArray(intArray: IntArray): ByteArray? {
        return null
    }

    private fun cleanByteArrayPixelDataToIntArray(cleanByteArrayPixelData: ByteArray): IntArray {
        val intArray = IntArray(cleanByteArrayPixelData.size / 4)
        var j = 0
        var i = 0
        while (i < cleanByteArrayPixelData.size) {
            val temp = ByteArray(4)
            temp[0] = cleanByteArrayPixelData[i]
            temp[1] = cleanByteArrayPixelData[i + 1]
            temp[2] = cleanByteArrayPixelData[i + 2]
            temp[3] = cleanByteArrayPixelData[i + 3]
            intArray[j] = bytesToInt(temp)
            j++
            i += 4
        }
        return intArray
    }

    private fun bytesToInt(bytes: ByteArray): Int {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).int
    }


    private fun shortArrayToIntArrat(shortArray: ShortArray): IntArray {
        val intArray = IntArray(shortArray.size)
        for (i in intArray.indices) {
            intArray[i] = shortArray[i].toInt()
        }
        return intArray
    }

    private fun shortArrayToByteArray(shortArray: ShortArray): ByteArray {
        val m : Double = 255.0 / Integer.parseInt(dicomAttributeCollection!!.getAttributeValue(TagFromName.WindowWidth))
        val x1 : Int = Integer.parseInt(dicomAttributeCollection!!.getAttributeValue(TagFromName.WindowCenter)) -
                Integer.parseInt(dicomAttributeCollection!!.getAttributeValue(TagFromName.WindowCenter)) / 2
        val b : Int = (- (m * x1)).toInt()

        val lut: HashMap<Short, Byte> = HashMap()
        val j: Int = Short.MIN_VALUE.toInt()
        while (j < Short.MAX_VALUE.toInt()) {
            var temp = ((m * j) + b).toInt()
            if(temp > 127) temp = 127
            else if (temp < -128) temp = -128
            lut[j.toShort()] = temp.toByte()
        }
        val byteArray = ByteArray(shortArray.size)
        for (i in shortArray.indices) {
            byteArray[i] = lut[shortArray[i]]!!
        }

        return byteArray
    }

    private fun cleanByteArrayPixelDataToShortArray(cleanByteArray : ByteArray): ShortArray {
        val shortArray = ShortArray(cleanByteArray.size / 2)
        var j = 0
        var i = 0
        while (i < cleanByteArray.size) {
            val temp = ByteArray(2)
            temp[0] = cleanByteArray[i]
            temp[1] = cleanByteArray[i + 1]
            shortArray[j] = bytesToShort(temp)
            j++
            i += 2
        }
        return shortArray
    }

    private fun bytesToShort(bytes: ByteArray): Short {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).short
    }

    private fun byteArrayToIntArray(byteArray: ByteArray) : IntArray {
        val intArray = IntArray(byteArray.size)
        for (i in intArray.indices) {
            intArray[i] = byteArray[i].toInt()
        }
        return intArray
    }

    private fun byteArrayToShortArray(byteArray: ByteArray) : ShortArray {
        val shortArray = ShortArray(byteArray.size)
        for (i in shortArray.indices) {
            shortArray[i] = byteArray[i].toShort()
        }
        return shortArray
    }

    private fun convertDirtyByteArrayToCleanByteArray(dirtyByteArrayPixelData: ByteArray) {
        cleanByteArrayPixelData = ByteArray(dirtyByteArrayPixelData.size - 144)
        for (i in cleanByteArrayPixelData!!.indices) {
            cleanByteArrayPixelData!![i] = dirtyByteArrayPixelData[i + 144]
        }
    }

    private fun readPixelDataFileAndWriteToDirtyByteArray(): ByteArray {
        val file = File("pixeldata.txt")
        val imageInputStream = FileImageInputStream(file)
        var dirtyByteArrayPixelData = ByteArray(file.length().toInt())
        imageInputStream.read(dirtyByteArrayPixelData)
        imageInputStream.close()
        return dirtyByteArrayPixelData!!
    }

    private fun pixelDataWriteToFile() {
        val pixelData = dicomAttributeCollection?.getAttributePixelData()
        val outputStream = FileOutputStream(File("src/test/resources/pixeldata.txt"))
        val transferSyntaxUID = dicomAttributeCollection?.getAttributeValue(TagFromName.TransactionUID)
        val dicomOutputStream = DicomOutputStream(outputStream, "", transferSyntaxUID)
        pixelData?.write(dicomOutputStream)
    }

    override fun getImage(): BufferedImage {
        return dicomAttributeCollection!!.buildHumanReadableImage()
    }

    override fun getImageDataAsShortArray(): ShortArray {
        return shortArray!!
    }

    override fun getImageDataAsByteArray(): ByteArray {
        return byteArray!!
    }

    override fun getImageDataAsIntArray(): IntArray {
        return intArray!!
    }

    override fun copy(): ImagingData<BufferedImage> {
        return DicomData(dicomAttributeCollection!!.clone())
    }

    override fun applyMask(mask: BooleanArray2D) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val width: Int
        get() = dicomAttributeCollection!!.buildHumanReadableImage().width
    override val height: Int
        get() = dicomAttributeCollection!!.buildHumanReadableImage().height
}