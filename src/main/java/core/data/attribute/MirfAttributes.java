package core.data.attribute;


import core.repository.RepositoryInfo;

/**
 * Stores {@link DataAttributeMockup} for all well-known MIRF data attributes
 */
public final class MirfAttributes {

    /**
     * todo: (avlomakin) add link to doc
     */
    public static final DataAttributeMockup<Boolean> THRESHOLDED = new DataAttributeMockup<>("Thresholded", "96969da6-e6c2-11e8-9f32-f2801f1b9fd1", AttributeTagType.UUID);

    public static final DataAttributeMockup<RepositoryInfo> REPO_INFO = new DataAttributeMockup<>("Repository info", "b38d7c64-ecf2-11e8-8eb2-f2801f1b9fd1", AttributeTagType.UUID);

    private MirfAttributes() {
    }
}