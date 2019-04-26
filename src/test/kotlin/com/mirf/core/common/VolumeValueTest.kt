package com.mirf.core.common

import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

class VolumeValueTest{


    @Test
    fun volumeAccumulator(){
        var volume = VolumeValue.zero
        val oneMM = VolumeValue.createFromMM3(1.0)

        for (i in 1..999)
            volume += oneMM
        Assert.assertEquals(volume.toString(), "999.0 mm³")

        volume += oneMM
        Assert.assertEquals(volume.toString(), "1.0 cm³")

    }

    @Test
    fun simple_UnitsAdjust(){
        val mm = VolumeValue.createFromMM3(1000.0, false)
        val cm = VolumeValue.createFromCM3(1.0)

        var sum = mm + cm
        Assert.assertEquals(sum.toString(), "2.0 cm³")

        sum = cm + mm
        Assert.assertEquals(sum.toString(), "2.0 cm³")
    }
}