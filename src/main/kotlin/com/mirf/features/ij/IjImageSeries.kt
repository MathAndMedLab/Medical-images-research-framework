package com.mirf.features.ij

import com.mirf.core.common.VolumeValue
import com.mirf.core.common.toBicolor
import com.mirf.core.data.MirfData
import com.mirf.core.data.attribute.MirfAttributes
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.data.medimage.MedImage
import com.mirf.core.repository.RepositoryCommander
import com.mirf.features.repository.LocalRepositoryCommander
import ij.ImagePlus
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class IjImageSeries(private val image: ImagePlus) : MirfData(), ImageSeries {

    init {
        //in mm
        val volume = image.fileInfo.pixelWidth * image.fileInfo.pixelHeight * image.fileInfo.pixelDepth

        attributes.add(MirfAttributes.ATOMIC_ELEMENT_VOLUME.new(VolumeValue.createFromMM3(volume)))
    }

    override fun dumpToRepository(repository: RepositoryCommander, name: String): String {

        if (repository is LocalRepositoryCommander) {
            log.info("file already exists on the local file system, path - \'${image.originalFileInfo.fileFullPath}\'")

            val originalPath = Paths.get(image.originalFileInfo.fileFullPath)

            val targetName = if (name == "") image.originalFileInfo.fileName else "$name.$extension"
            val target = repository.workingDir.resolve(targetName)
            val result = Files.copy(originalPath, target)

            return result.toString()
        }
        TODO("Not implemented for non-local systems")
    }

    override fun applyMask(masks: ImageSeries) {
        if (images.size != masks.images.size)
            throw IllegalArgumentException("number of masks doesn't match number of images")

        for (i in 0 until images.size) {
            val binaryMask = masks.images[i].image!!.toBicolor()
            images[i].attributes.add(MirfAttributes.IMAGE_SEGMENTATION_MASK.new(binaryMask))
        }
    }

    override fun getFileBytes(): ByteArray {
        return File(image.fileInfo.fileFullPath).readBytes()
    }

    val extension: String = image.originalFileInfo.fileName.substringAfterLast('.')

    private val _images = lazy { createImages() }

    private fun createImages(): List<MedImage> {
        return List(image.stackSize) { x -> IjMedImage(image.stack.getProcessor(x + 1), extension) }
    }

    override val images: List<MedImage>
        get() = _images.value

    override public fun clone(): IjImageSeries {
        return IjImageSeries(image.clone() as ImagePlus)
    }

}

