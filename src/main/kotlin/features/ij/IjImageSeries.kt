package features.ij

import core.data.MirfData
import core.data.medimage.ImageSeries
import core.data.medimage.MedImage
import core.repository.RepositoryCommander
import features.repository.LocalRepositoryCommander
import ij.ImagePlus
import java.io.File
import java.nio.file.Paths

class IjImageSeries(private val image: ImagePlus) : MirfData(), ImageSeries {
    override fun dumpToRepository(repository: RepositoryCommander): String {

        if (repository is LocalRepositoryCommander) {
            log.info("file already exists on the local file system, path - \'${image.originalFileInfo.fileFullPath}\'")
            return image.originalFileInfo.fileFullPath
        }
        TODO("Not implemented for non-local systems")
    }

    override fun getFileBytes(): ByteArray {
        return File(image.fileInfo.fileFullPath).readBytes()
    }

    val extension: String = image.fileInfo.fileFormatString

    private val _images = lazy { createImages() }

    private fun createImages(): List<MedImage> {
        return List(image.stackSize) { x -> IjMedImage(image.stack.getProcessor(x + 1), extension) }
    }

    override val images: List<MedImage>
        get() = _images.value

}