package core.data.attribute


import core.array.deepCopy
import core.data.medimage.ImagingData
import core.repository.RepositoryInfo
import java.awt.image.BufferedImage

/**
 * Stores [DataAttributeMockup] for all well-known MIRF data attributes
 */
object MirfAttributes {

    /**
     * todo: (avlomakin) add link to doc
     */
    val THRESHOLDED = DataAttributeMockup<Switch.Instance>("Thresholded", "96969da6-e6c2-11e8-9f32-f2801f1b9fd1", AttributeTagType.UUID)

    val REPO_INFO = DataAttributeMockup<RepositoryInfo>("Repository info", "b38d7c64-ecf2-11e8-8eb2-f2801f1b9fd1", AttributeTagType.UUID, RepositoryInfo::copy)

    val IMAGE_SEGMENTATION_MASK = DataAttributeMockup<Array<ByteArray>>("Segmentation mask", "f0bd93ba-7ce1-4c3a-9532-7683bca128a2", AttributeTagType.UUID) {x -> x.deepCopy()}

    val IMAGING_DATA = DataAttributeMockup<ImagingData<*>>("Image raw data", "08028d56-330e-414a-a898-05cd830d6d59", AttributeTagType.UUID) { x -> x.copy()}

    val PATIENT_NAME = DataAttributeMockup<String>("Patient name ", "f77da689-e061-4d48-8335-5902bf3f3a0c", AttributeTagType.UUID) { x -> x}

}