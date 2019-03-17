package core.data.medimage

import core.data.Data
import core.repository.RepositoryCommander

interface ImageSeries : Data {
    val images: List<MedImage>

    fun getFileBytes(): ByteArray

    fun dumpToRepository(repository: RepositoryCommander) : String
}