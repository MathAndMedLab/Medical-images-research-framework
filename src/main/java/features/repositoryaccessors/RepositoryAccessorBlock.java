package features.repositoryaccessors;

import core.algorithm.Algorithm;
import core.data.Data;
import core.pipeline.PipelineBlock;
import core.repository.Repository;
import features.repositoryaccessors.data.RepoRequest;

public class RepositoryAccessorBlock<I extends Data, O extends Data> extends PipelineBlock<I, O> {

    private Repository repo;
    private Algorithm<RepoRequest, O> algorithm;

    public boolean enabled = true;
    private String link;

    public RepositoryAccessorBlock(Repository repo, Algorithm<RepoRequest, O> algorithm, String link) {
        this.repo = repo;
        this.algorithm = algorithm;
        this.link = link;
    }

    @Override
    public void inputDataReady(PipelineBlock<?, I> sender, I input) {
        if(enabled) {
            O result = algorithm.execute(new RepoRequest(link, repo){{bundle = input;}});
            notifyListeners(this, result);
        }
    }
}
