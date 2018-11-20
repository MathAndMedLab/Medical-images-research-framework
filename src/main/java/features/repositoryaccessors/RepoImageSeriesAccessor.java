package features.repositoryaccessors;

import core.algorithm.Algorithm;
import core.data.medimage.ImageSeries;
import features.repositoryaccessors.data.RepoRequest;
import core.pipeline.impl.AlgorithmHostBlock;

/**
 * Class to inject in pipeline via {@link AlgorithmHostBlock}, retrieves ImageSeries using provided RepoRequest
 */
public class RepoImageSeriesAccessor implements Algorithm<RepoRequest, ImageSeries> {
    @Override
    public ImageSeries execute(RepoRequest input) {
        return input.getRepository().getImageSeries(input.getLink());
    }
}
