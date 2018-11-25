package features.repositoryaccessors.data;

import core.data.Data;
import core.repository.RepositoryCommander;

/**
 * Class used by Repository accessors. Contains information about repository and link for request
 */
public class RepoRequest extends Data {

    private String link;

    private RepositoryCommander repositoryCommander;

    public Object bundle;

    public RepoRequest(String link, RepositoryCommander repositoryCommander) {
        this.link = link;
        this.repositoryCommander = repositoryCommander;
    }

    public String getLink() {
        return link;
    }

    public RepositoryCommander getRepositoryCommander() {
        return repositoryCommander;
    }
}
