package core.data.medimage

import core.data.AttributeCollection
import core.data.MirfData
import core.repository.RepositoryCommander

/**
 * ImageSeries stores a list of [MedImage]
 */
class MirfImageSeries(override val images: List<MedImage>, attributes: AttributeCollection = AttributeCollection()) : MirfData(attributes), ImageSeries {
    override fun applyMask(masks: ImageSeries) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFileBytes(): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dumpToRepository(repository: RepositoryCommander) : String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
