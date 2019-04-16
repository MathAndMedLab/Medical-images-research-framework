package com.mirf.core.array

import com.mirf.core.data.MirfException

/**
 * Represents 2 dimensional fixed size array of [Short]
 */
class ShortArray2D private constructor(
        array: Array<ShortArray>,
        val rows: Int, val columns: Int) {

    private val _array: Array<ShortArray>

    init {
        if (!isArrayValid(array))
            throw MirfException("failed to create image data: array is in invalid state")

        this._array = array
    }

    private fun isArrayValid(array: Array<ShortArray>): Boolean {
        return array.size == rows && array.all { x -> x.size == columns }
    }

    operator fun get(index: Int): ShortArray = _array[index]

    fun deepCopy(): ShortArray2D {
        return ShortArray2D(_array.deepCopy(), rows, columns)
    }

    fun to1D(): ShortArray = _array.to1D()

    companion object {
        /**
         * Creates empty [ShortArray2D] with size of [rows]x[columns]
         */
        fun create(rows: Int, columns: Int): ShortArray2D {
            val array = Array(rows) { ShortArray(columns) }
            return ShortArray2D(array, rows, columns)
        }

        /**
         * Creates [ShortArray2D] with size of [rows]x[columns] and fills it with values from source array
         */
        fun create(rows: Int, columns: Int, sourceArray: ShortArray): ShortArray2D {

            if (sourceArray.size < rows * columns)
                throw MirfException("Not enough elements in source array. Presented ${sourceArray.size}, required ${columns * rows}")

            val array = Array(rows) { x -> sourceArray.copyOfRange(x * columns, (x + 1) * columns) }
            return ShortArray2D(array, rows, columns)
        }
    }
}