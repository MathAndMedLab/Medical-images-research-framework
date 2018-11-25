package core.repository;

/**
 * Info about {@link RepositoryCommander}. Used for reports generation
 */
public class RepositoryInfo {
    public final String repositoryName;
    public final String username;

    public RepositoryInfo(String repositoryName, String username) {
        this.repositoryName = repositoryName;
        this.username = username;
    }
}
