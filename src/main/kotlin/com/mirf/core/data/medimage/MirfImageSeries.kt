package com.mirf.core.data.medimage

import com.mirf.core.data.AttributeCollection
import com.mirf.core.data.MirfData
import com.mirf.core.repository.RepositoryCommander

/**
 * ImageSeries stores a list of [MedImage]
 */
class MirfImageSeries(override val images: List<MedImage>, attributes: AttributeCollection = AttributeCollection()) : MirfData(attributes), ImageSeries {
    override fun clone(): MirfImageSeries {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun applyMask(masks: ImageSeries) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFileBytes(): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dumpToRepository(repository: RepositoryCommander, name: String) : String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
