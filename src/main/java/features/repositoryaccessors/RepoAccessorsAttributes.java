package features.repositoryaccessors;

import core.data.attribute.AttributeTagType;
import core.data.attribute.DataAttributeMockup;

/**
 * Stores {@link DataAttributeMockup} for all well-known repository accessors data attributes
 */
public final class RepoAccessorsAttributes {

    public static final DataAttributeMockup<RepositoryRequestInfo> REPOSITORY_REQUEST_INFO =
            new DataAttributeMockup<>("Repository request", "8285b554-ecf3-11e8-8eb2-f2801f1b9fd1", AttributeTagType.UUID);

    private RepoAccessorsAttributes() {
    }
}
