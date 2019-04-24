package com.mirf.core.data.medimage

import com.mirf.core.data.Data
import com.mirf.core.repository.RepositoryCommander

interface ImageSeries : Data {
    val images: List<MedImage>

    fun getFileBytes(): ByteArray

    fun dumpToRepository(repository: RepositoryCommander) : String
    fun applyMask(masks: ImageSeries)
}