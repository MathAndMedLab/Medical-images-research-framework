package com.mirf.features.dicomimage.data
import com.pixelmed.dicom.TagFromName
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Convert byte array to short array
 */
fun byteArrayToShortArray(byteArray: ByteArray) : ShortArray {
    val shortArray = ShortArray(byteArray.size)
    for (i in shortArray.indices) {
        shortArray[i] = byteArray[i].toShort()
    }
    return shortArray
}


/**
 * Convert image data from file to int array
 */
fun cleanByteArrayPixelDataToIntArray(cleanByteArrayPixelData: ByteArray): IntArray {
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

/**
 * Convert byte array to int value
 */
fun bytesToInt(bytes: ByteArray): Int {
    return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).int
}

/**
 * Convert short array to int array
 */
fun shortArrayToIntArray(shortArray: ShortArray): IntArray {
    val intArray = IntArray(shortArray.size)
    for (i in intArray.indices) {
        intArray[i] = shortArray[i].toInt()
    }
    return intArray
}


/**
 * Convert image data from file to int array
 */
fun cleanByteArrayPixelDataToShortArray(cleanByteArray : ByteArray): ShortArray {
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

/**
 * Convert byte array to short value
 */
fun bytesToShort(bytes: ByteArray): Short {
    return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).short
}

/**
 * Convert byte array to int array
 */
fun byteArrayToIntArray(byteArray: ByteArray) : IntArray {
    val intArray = IntArray(byteArray.size)
    for (i in intArray.indices) {
        intArray[i] = byteArray[i].toInt()
    }
    return intArray
}

/**
 * Convert short array to byte array use "grayscale standard display function"
 */
fun shortArrayToByteArray(shortArray: ShortArray, dicomAttributeCollection : DicomAttributeCollection): ByteArray {
    val m : Double = 255.0 / Integer.parseInt(dicomAttributeCollection.getAttributeValue(TagFromName.WindowWidth))
    val x1 : Int = Integer.parseInt(dicomAttributeCollection.getAttributeValue(TagFromName.WindowCenter)) -
            Integer.parseInt(dicomAttributeCollection.getAttributeValue(TagFromName.WindowCenter)) / 2
    val b : Int = (- (m * x1)).toInt()

    val lut: HashMap<Short, Byte> = HashMap()
    val min : Int = Integer.parseInt(dicomAttributeCollection.getAttributeValue(TagFromName.SmallestImagePixelValue))
    val max : Int = Integer.parseInt(dicomAttributeCollection.getAttributeValue(TagFromName.LargestImagePixelValue))
    var j: Int = min
    while (j <= max) {
        var temp = ((m * j) + b).toInt()
        if(temp > 127) temp = 127
        else if (temp < -128) temp = -128
        lut[j.toShort()] = temp.toByte()
        j++
    }
    val byteArray = ByteArray(shortArray.size)
    for (i in shortArray.indices) {
        val byte = lut[shortArray[i]] as Byte
        byteArray[i] = byte;
    }

    return byteArray
}