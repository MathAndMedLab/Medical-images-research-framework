package com.mirf.core.array

import com.mirf.core.data.MirfException

/**
 * Represents 2 dimensional fixed size array of [Byte]
 */
class ByteArray2D private constructor(
        array: Array<ByteArray>,
        val rows: Int, val columns: Int) {

    private val _array: Array<ByteArray>

    init {
        if (!isArrayValid(array))
            throw MirfException("failed to create image data: array is in invalid state")

        this._array = array
    }

    private fun isArrayValid(array: Array<ByteArray>): Boolean {
        return array.size == rows && array.all { x -> x.size == columns }
    }

    fun deepCopy(): ByteArray2D {
        return ByteArray2D(_array.deepCopy(), rows, columns)
    }

    fun to1D(): ByteArray = _array.to1D()

    operator fun get(index: Int): ByteArray = _array[index]

    companion object {
        /**
         * Creates empty [ByteArray2D] with size of [rows]x[columns]
         */
        fun create(rows: Int, columns: Int): ByteArray2D {
            val array = Array(rows) { ByteArray(columns) }
            return ByteArray2D(array, rows, columns)
        }

        /**
         * Creates [ByteArray2D] with size of [rows]x[columns] and fills it with values from source array
         */
        fun create(rows: Int, columns: Int, sourceArray: ByteArray): ByteArray2D {

            if (sourceArray.size < rows * columns)
                throw MirfException("Not enough elements in source array. Presented ${sourceArray.size}, required ${columns * rows}")

            val array = Array(rows) { x -> sourceArray.copyOfRange(x * columns, (x + 1) * columns - 1) }
            return ByteArray2D(array, rows, columns)
        }
    }
}