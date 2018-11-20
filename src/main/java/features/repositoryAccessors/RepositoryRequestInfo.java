package features.repositoryAccessors;

public class RepositoryRequestInfo {

    public final String link;
    public final RepositoryRequestType requestType;

    public RepositoryRequestInfo(String link, RepositoryRequestType requestType) {
        this.link = link;
        this.requestType = requestType;
    }
}

