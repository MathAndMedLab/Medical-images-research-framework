package com.mirf.features.ij

import com.mirf.core.data.medimage.ImageSeries
import ij.ImagePlus
import ij.io.FileInfo
import java.nio.file.Paths

fun ImagePlus.asImageSeries(): ImageSeries = IjImageSeries(this)

val FileInfo.fileFormatString: String
    get() = when (this.fileFormat) {
        1 -> "raw"
        else -> "unknown"
    }

val FileInfo.fileFullPath: String
    get() = Paths.get(this.directory, this.fileName).toString()
