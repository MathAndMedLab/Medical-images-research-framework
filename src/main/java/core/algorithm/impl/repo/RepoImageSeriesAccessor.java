package core.algorithm.impl.repo;

import core.algorithm.Algorithm;
import model.data.ImageSeries;
import model.data.RepoRequest;

public class RepoImageSeriesAccessor implements Algorithm<RepoRequest, ImageSeries> {

    @Override
    public ImageSeries execute(RepoRequest input) {
        return input.getRepo().GetImageSeries(input.getLink());
    }
}
