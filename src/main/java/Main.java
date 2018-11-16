import core.pipeline.impl.ConsumerBlock;
import core.pipeline.impl.SequentialPipeline;
import features.repository.LocalDirectoryRepository;
import features.imageFilters.ImageSeriesThresholdBlock;
import features.numInfoFromImage.ImageSeriesVoxelVolumeCalcAlg;
import features.repositoryAccessors.RepoImageSeriesAccessor;
import features.repositoryAccessors.data.RepoRequest;

import java.nio.file.Paths;

public class Main {

    public static void main (String args []) {

        SequentialPipeline pipe = new SequentialPipeline();

        pipe.add(new RepoImageSeriesAccessor());
        pipe.add(new ImageSeriesThresholdBlock((byte) 1, (byte) 2));
        pipe.add(new ImageSeriesVoxelVolumeCalcAlg());
        pipe.add(new ConsumerBlock(x -> System.out.println(x.data.toString())));

        RepoRequest init = new RepoRequest(Paths.get("c:", "src","data").toString(), new LocalDirectoryRepository());

        pipe.run(init);
        System.out.println ("Pipeline finished");
    }

}
