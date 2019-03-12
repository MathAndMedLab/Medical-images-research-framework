package features.ij

import core.data.medimage.ImageSeries
import ij.ImagePlus
import ij.io.FileInfo

fun ImagePlus.asImageSeries(): ImageSeries = IjImageSeries(this)

val FileInfo.fileFormatString: String
    get() = when (this.fileFormat) {
        1 -> "raw"
        else -> "unknown"
    }
