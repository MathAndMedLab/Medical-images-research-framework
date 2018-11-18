package cases;

import core.pipeline.impl.SequentialPipeline;
import features.reports.PassThroughBlock;
import features.reports.pdf.RepositoryAccessorPdfReporter;
import features.repository.LocalDirectoryRepository;
import features.repositoryAccessors.RepoFileSaver;
import features.repositoryAccessors.RepoImageSeriesAccessor;
import features.repositoryAccessors.RepositoryAccessorBlock;
import features.repositoryAccessors.data.RepoRequest;

import java.nio.file.Paths;

public class LocalRepoPdf {
    public void exec()
    {
        SequentialPipeline pipe = new SequentialPipeline();

        pipe.add(new RepoImageSeriesAccessor());
        PassThroughBlock pdfBlock = new PassThroughBlock(new RepositoryAccessorPdfReporter("report"));
        pipe.add(pdfBlock);

        RepositoryAccessorBlock repoBlock = new RepositoryAccessorBlock(new LocalDirectoryRepository(),
                new RepoFileSaver(), "c:\\src\\reports");

        pipe.add(repoBlock);
        RepoRequest init = new RepoRequest(Paths.get("c:", "src","data").toString(), new LocalDirectoryRepository());

        pipe.run(init);
        System.out.println ("Pipeline finished");
    }

}
