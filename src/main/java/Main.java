import core.algorithm.impl.img.ImageSeriesThresholdBlock;
import core.algorithm.impl.num.ImageSeriesVoxelVolumeCalcAlg;
import core.algorithm.impl.repo.RepoImageSeriesAccessor;
import core.pipeline.AlgorithmHostBlock;
import core.pipeline.SequentialPipeline;
import core.repo.LocalDirectoryRepo;
import model.data.Data;
import model.data.ImageSeries;
import model.data.RepoRequest;
import model.pipeline.PipelineBlock;

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
