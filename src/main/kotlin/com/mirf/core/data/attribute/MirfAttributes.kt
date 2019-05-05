package com.mirf.core.data.attribute


import com.mirf.core.array.BooleanArray2D
import com.mirf.core.common.VolumeValue
import com.mirf.core.data.medimage.ImagingData
import com.mirf.core.repository.RepositoryInfo
import java.awt.image.BufferedImage

/**
 * Stores [DataAttributeMockup] for all well-known MIRF data attributes
 */
object MirfAttributes {

    /**
     * todo: (avlomakin) add link to doc
     */
    val THRESHOLDED: DataAttributeMockup<Unit> =
            DataAttributeMockup("Thresholded", "96969da6-e6c2-11e8-9f32-f2801f1b9fd1", AttributeTagType.UUID)

    val REPO_INFO: DataAttributeMockup<RepositoryInfo> =
            DataAttributeMockup("Repository info", "b38d7c64-ecf2-11e8-8eb2-f2801f1b9fd1", AttributeTagType.UUID, RepositoryInfo::copy)

    val IMAGE_SEGMENTATION_MASK: DataAttributeMockup<BooleanArray2D> =
            DataAttributeMockup("Segmentation mask", "f0bd93ba-7ce1-4c3a-9532-7683bca128a2", AttributeTagType.UUID) { x -> x.deepCopy() }

    val IMAGING_DATA: DataAttributeMockup<ImagingData<*>> =
            DataAttributeMockup("Image raw data", "08028d56-330e-414a-a898-05cd830d6d59", AttributeTagType.UUID) { x -> x.copy() }

    val PATIENT_NAME: DataAttributeMockup<String> =
            DataAttributeMockup("Patient name ", "f77da689-e061-4d48-8335-5902bf3f3a0c", AttributeTagType.UUID) { x -> x }

    val ATOMIC_ELEMENT_VOLUME: DataAttributeMockup<VolumeValue> =
            DataAttributeMockup("Atomic element volume", "e305db33-a2c9-49e2-9dd1-135928ff5df1", AttributeTagType.UUID) {x ->x.copy()}

}