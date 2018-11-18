package features.repositoryAccessors;

import core.algorithm.Algorithm;
import core.data.Data;
import core.pipeline.PipelineBlock;
import core.repository.Repository;
import features.repositoryAccessors.data.RepoRequest;

public class RepositoryAccessorBlock<Input extends Data, Output extends Data> extends PipelineBlock<Input,Output> {

    private Repository repo;
    private Algorithm<RepoRequest, Output> algorithm;

    public boolean enabled = true;
    private String link;

    public RepositoryAccessorBlock(Repository repo, Algorithm<RepoRequest, Output> algorithm, String link) {
        this.repo = repo;
        this.algorithm = algorithm;
        this.link = link;
    }

    @Override
    public void InputDataReady(PipelineBlock<?, Input> sender, Input input) {
        if(enabled) {
            Output result = algorithm.execute(new RepoRequest(link, repo){{bundle = input;}});
            notifyListeners(this, result);
        }
    }
}
