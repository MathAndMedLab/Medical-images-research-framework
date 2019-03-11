package core.data.medimage

import core.data.Data

interface ImageSeries : Data {
    val images: List<MedImage>
}