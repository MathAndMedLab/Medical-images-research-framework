package core.data.attribute


import core.repository.RepositoryInfo

/**
 * Stores [DataAttributeMockup] for all well-known MIRF data attributes
 */
object MirfAttributes {

    /**
     * todo: (avlomakin) add link to doc
     */
    val THRESHOLDED = DataAttributeMockup<Switch.Instance>("Thresholded", "96969da6-e6c2-11e8-9f32-f2801f1b9fd1", AttributeTagType.UUID)

    val REPO_INFO = DataAttributeMockup<RepositoryInfo>("Repository info", "b38d7c64-ecf2-11e8-8eb2-f2801f1b9fd1", AttributeTagType.UUID)

    val IMAGE_SERIES_MASK = DataAttributeMockup<Array<Boolean>>("Image mask", "f0bd93ba-7ce1-4c3a-9532-7683bca128a2", AttributeTagType.UUID)
}