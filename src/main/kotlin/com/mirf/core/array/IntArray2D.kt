package com.mirf.core.array

import com.mirf.core.data.MirfException

/**
 * Represents 2 dimensional fixed size array of [Int]
 */
class IntArray2D private constructor(
        array: Array<IntArray>,
        val rows: Int, val columns: Int) {

    private val _array: Array<IntArray>

    init {
        if (!isArrayValid(array))
            throw MirfException("failed to create image data: array is in invalid state")

        this._array = array
    }

    private fun isArrayValid(array: Array<IntArray>): Boolean {
        return array.size == rows && array.all { x -> x.size == columns }
    }

    fun deepCopy(): IntArray2D {
        return IntArray2D(_array.deepCopy(), rows, columns)
    }

    fun to1D(): IntArray = _array.to1D()

    operator fun get(index: Int): IntArray = _array[index]

    companion object {
        /**
         * Creates empty [IntArray2D] with size of [rows]x[columns]
         */
        fun create(rows: Int, columns: Int): IntArray2D {
            val array = Array(rows) { IntArray(columns) }
            return IntArray2D(array, rows, columns)
        }

        /**
         * Creates [IntArray2D] with size of [rows]x[columns] and fills it with values from source array
         */
        fun create(rows: Int, columns: Int, sourceArray: IntArray): IntArray2D {

            if (sourceArray.size < rows * columns)
                throw MirfException("Not enough elements in source array. Presented ${sourceArray.size}, required ${columns * rows}")

            val array = Array(rows) { x -> sourceArray.copyOfRange(x * columns, (x + 1) * columns - 1) }
            return IntArray2D(array, rows, columns)
        }
    }
}