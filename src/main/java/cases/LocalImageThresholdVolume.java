package cases;

import core.pipeline.impl.ConsumerBlock;
import core.pipeline.impl.SequentialPipeline;
import features.imagefilters.ImageSeriesThresholdBlock;
import features.numinfofromimage.ImageSeriesVoxelVolumeCalcAlg;
import features.repository.LocalDirectoryRepository;
import features.repositoryaccessors.RepoImageSeriesAccessor;
import features.repositoryaccessors.data.RepoRequest;

import java.nio.file.Paths;

public class LocalImageThresholdVolume {
    public void exec(){
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
