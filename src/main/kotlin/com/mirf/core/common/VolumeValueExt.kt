package com.mirf.core.common

fun Collection<VolumeValue>.sum(): VolumeValue {
    var result = VolumeValue.zero
    for (value in this)
        result += value
    return result
}