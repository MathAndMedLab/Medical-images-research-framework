package features.repositoryaccessors;

import core.algorithm.Algorithm;
import core.data.medimage.ImageSeries;
import core.data.medimage.MirfAttributes;
import core.pipeline.impl.AlgorithmHostBlock;
import core.repository.RepositoryInfo;
import features.repositoryaccessors.data.RepoRequest;

/**
 * Class to inject in pipeline via {@link AlgorithmHostBlock}, retrieves ImageSeries using provided RepoRequest
 */
public class RepoImageSeriesAccessor implements Algorithm<RepoRequest, ImageSeries> {

    @Override
    public ImageSeries execute(RepoRequest repoRequest) {
        ImageSeries images = repoRequest.getRepository().getImageSeries(repoRequest.getLink());

        RepositoryInfo repositoryInfo = new RepositoryInfo(repoRequest.getRepository().getClass().getSimpleName(), "TODO: (avlomakin)");
        images.addAttribute(MirfAttributes.REPO_INFO.createAttribute(repositoryInfo));

        RepositoryRequestInfo requestInfo = new RepositoryRequestInfo(repoRequest.getLink(), RepositoryRequestType.GET);
        images.addAttribute(RepoAccessorsAttributes.REPOSITORY_REQUEST_INFO.createAttribute(requestInfo));

        return repoRequest.getRepository().getImageSeries(repoRequest.getLink());
    }
}
