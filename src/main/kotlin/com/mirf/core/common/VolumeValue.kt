package com.mirf.core.common

import com.mirf.core.data.MirfException
import kotlin.math.absoluteValue

class VolumeValue private constructor(val value: Double, val unit: VolumeUnit) {
    enum class VolumeUnit(val minUnits: Int) {
        MM3(1),
        CM3(1000),
        M3(100000000);

        override fun toString(): String {
            return when (this) {
                MM3 -> "mm³"
                CM3 -> "cm³"
                M3 -> "m³"
                else -> throw MirfException("undefined volume unit ${this.name}")
            }
        }

        companion object {
            val maxUnit: VolumeUnit = M3
        }
    }

    override fun toString(): String {
        return "$value $unit"
    }

    fun toString(decimalPlaces: Int): String {
        return String.format("%.${decimalPlaces}f $unit", value)
    }

    fun copy(): VolumeValue = VolumeValue(value, unit)

    operator fun compareTo(other: VolumeValue): Int {
        return this.asMinimalUnit.compareTo(other.asMinimalUnit)
    }

    private val asMinimalUnit: Double = value * unit.minUnits

    operator fun plus(other: VolumeValue): VolumeValue {
        val value = this.asMinimalUnit + other.asMinimalUnit
        return createAdjustedFromMinimalUnit(value)
    }

    operator fun minus(other: VolumeValue): VolumeValue {
        val value = this.asMinimalUnit - other.asMinimalUnit
        return createAdjustedFromMinimalUnit(value)
    }

    companion object {
        val zero: VolumeValue = VolumeValue(0.0, VolumeUnit.MM3)

        fun createFromMM3(value: Double, autoAdjust: Boolean = true): VolumeValue {
            if (autoAdjust) {
                return createAdjustedFromMinimalUnit(value)
            }
            return VolumeValue(value, VolumeUnit.MM3)
        }

        private fun createAdjustedFromMinimalUnit(value: Double): VolumeValue {
            //minimal - MM3
            if (value.absoluteValue < VolumeUnit.CM3.minUnits)
                return VolumeValue(value, VolumeUnit.MM3)

            if (value.absoluteValue < VolumeUnit.M3.minUnits)
                return VolumeValue(value / VolumeUnit.CM3.minUnits, VolumeUnit.CM3)

            return VolumeValue(value / VolumeUnit.M3.minUnits, VolumeUnit.M3)
        }

        fun createFromCM3(value: Double, autoAdjust: Boolean = true): VolumeValue {
            if (autoAdjust) {
                return createAdjustedFromMinimalUnit(value * VolumeUnit.CM3.minUnits)
            }
            return VolumeValue(value, VolumeUnit.CM3)
        }

        fun createFromM3(value: Double, autoAdjust: Boolean = true): VolumeValue {
            if (autoAdjust) {
                return createAdjustedFromMinimalUnit(value * VolumeUnit.M3.minUnits)
            }
            return VolumeValue(value, VolumeUnit.M3)
        }
    }
}