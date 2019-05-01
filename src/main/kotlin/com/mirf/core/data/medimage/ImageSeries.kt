package com.mirf.core.data.medimage

import com.mirf.core.data.Data
import com.mirf.core.repository.RepositoryCommander

interface ImageSeries : Data, Cloneable {
    val images: List<MedImage>

    fun getFileBytes(): ByteArray

    /***
     * saves series on repository
     * @param repository target repository
     * @param name file name without extension. If empty string is passed, image series will use internal file name
     * @return link to access the saved file(s)
     */
    fun dumpToRepository(repository: RepositoryCommander, name: String = ""): String
    fun applyMask(masks: ImageSeries)
    override public fun clone(): ImageSeries
}