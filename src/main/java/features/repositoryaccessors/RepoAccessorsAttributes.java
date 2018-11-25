package features.repositoryaccessors;

import core.data.medimage.AttributeTagType;
import core.data.medimage.MirfAttributeMockup;

/**
 * Stores {@link MirfAttributeMockup} for all well-known repository accessors data attributes
 */
public final class RepoAccessorsAttributes {

    public static final MirfAttributeMockup<RepositoryRequestInfo> REPOSITORY_REQUEST_INFO =
            new MirfAttributeMockup<>("Repository request", "8285b554-ecf3-11e8-8eb2-f2801f1b9fd1", AttributeTagType.UUID);

    private RepoAccessorsAttributes() {
    }
}
