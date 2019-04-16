package com.mirf.core.array

import com.mirf.core.data.MirfException

/**
 * Represents 2 dimensional fixed size array of [Boolean]
 */
class BooleanArray2D private constructor(
        array: Array<BooleanArray>,
        val rows: Int, val columns: Int) {

    private val _array: Array<BooleanArray>

    init {
        if (!isArrayValid(array))
            throw MirfException("failed to create image data: array is in invalid state")

        this._array = array
    }

    private fun isArrayValid(array: Array<BooleanArray>): Boolean {
        return array.size == rows && array.all { x -> x.size == columns }
    }

    fun deepCopy(): BooleanArray2D {
        return BooleanArray2D(_array.deepCopy(), rows, columns)
    }

    fun to1D(): BooleanArray = _array.to1D()

    operator fun get(index: Int): BooleanArray = _array[index]

    companion object {
        /**
         * Creates empty [BooleanArray2D] with size of [rows]x[columns]
         */
        fun create(rows: Int, columns: Int): BooleanArray2D {
            val array = Array(rows) { BooleanArray(columns) }
            return BooleanArray2D(array, rows, columns)
        }

        /**
         * Creates [BooleanArray2D] with size of [rows]x[columns] and fills it with values from source array
         */
        fun create(rows: Int, columns: Int, sourceArray: BooleanArray): BooleanArray2D {

            if (sourceArray.size < rows * columns)
                throw MirfException("Not enough elements in source array. Presented ${sourceArray.size}, required ${columns * rows}")

            val array = Array(rows) { x -> sourceArray.copyOfRange(x * columns, (x + 1) * columns - 1) }
            return BooleanArray2D(array, rows, columns)
        }
    }
}