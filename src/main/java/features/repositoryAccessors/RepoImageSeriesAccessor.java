package features.repositoryAccessors;

import core.algorithm.Algorithm;
import core.data.medImage.ImageSeries;
import features.repositoryAccessors.data.RepoRequest;

// TODO(avlomakin): add javadoc here
public class RepoImageSeriesAccessor implements Algorithm<RepoRequest, ImageSeries> {

    @Override
    public ImageSeries execute(RepoRequest input) {
        return input.getRepo().GetImageSeries(input.getLink());
    }
}
