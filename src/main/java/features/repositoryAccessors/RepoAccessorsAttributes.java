package features.repositoryAccessors;

import core.data.medImage.AttributeTagType;
import core.data.medImage.MirfAttributeMockup;

public final class RepoAccessorsAttributes {

    public static final MirfAttributeMockup<RepositoryRequestInfo> REPOSITORY_REQUEST_INFO =
            new MirfAttributeMockup<>("Repository request", "8285b554-ecf3-11e8-8eb2-f2801f1b9fd1", AttributeTagType.UUID);

    private RepoAccessorsAttributes() {
    }
}
