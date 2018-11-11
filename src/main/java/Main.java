import features.imageFilters.ImageSeriesThresholdBlock;
import features.numInfoFromImage.ImageSeriesVoxelVolumeCalcAlg;
import features.repositoryAccessors.RepoImageSeriesAccessor;
import core.pipeline.impl.AlgorithmHostBlock;
import core.pipeline.impl.SequentialPipeline;
import core.repository.LocalDirectoryRepo;
import core.data.Data;
import core.data.medImage.ImageSeries;
import features.repositoryAccessors.data.RepoRequest;
import core.pipeline.PipelineBlock;

import java.util.ArrayList;

public class Main {

    public static void main (String args []) throws NoSuchMethodException {

        AlgorithmHostBlock<RepoRequest, ImageSeries> repoAccessorHost = new AlgorithmHostBlock<>(new RepoImageSeriesAccessor());
        AlgorithmHostBlock<ImageSeries, ImageSeries> imageSeriesThresholder = new AlgorithmHostBlock<>(new ImageSeriesThresholdBlock((byte) 1, (byte) 2));
        AlgorithmHostBlock<ImageSeries, Data> volumeCalculator = new AlgorithmHostBlock<>(new ImageSeriesVoxelVolumeCalcAlg());

        SequentialPipeline pipe = SequentialPipeline.CreateFromBlockList(new ArrayList<PipelineBlock>() {{
            add(repoAccessorHost);
            add(imageSeriesThresholder);
            add(volumeCalculator);
        }}, false);

        RepoRequest init = new RepoRequest(new LocalDirectoryRepo(), "c:\\src");

        pipe.Run(init);
        System.out.println ("Pipeline finished");
    }

}
