package features.repositoryaccessors;

import core.algorithm.Algorithm;
import core.data.Data;
import core.pipeline.PipelineBlock;
import core.repository.RepositoryCommander;
import features.repositoryaccessors.data.RepoRequest;

/**
 * {@link PipelineBlock} that stores all necessary information about repository such as {@link RepositoryCommander},
 * connection string and Repository accessor algorithm
 * @param <I> Input type
 * @param <O> Output type
 */
public class RepositoryAccessorBlock<I extends Data, O extends Data> extends PipelineBlock<I, O> {

    private RepositoryCommander repo;
    private Algorithm<RepoRequest, O> algorithm;

    public boolean enabled = true;
    private String connectionString;

    public RepositoryAccessorBlock(RepositoryCommander repo, Algorithm<RepoRequest, O> algorithm, String connectionString) {
        this.repo = repo;
        this.algorithm = algorithm;
        this.connectionString = connectionString;
    }

    @Override
    public void inputDataReady(PipelineBlock<?, I> sender, I input) {
        if(enabled) {
            O result = algorithm.execute(new RepoRequest(connectionString, repo){{bundle = input;}});
            notifyListeners(this, result);
        }
    }
}
