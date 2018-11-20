package cases;

import core.pipeline.impl.AccumulatorWithAlgBlock;
import core.pipeline.impl.SequentialPipeline;
import features.reports.creators.RepoAccessorReportCreator;
import features.reports.pdf.MirfReportToPdfElementConverter;
import features.reports.pdf.PdfElementsAccumulator;
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
        pipe.add(new MirfReportToPdfElementConverter<>(new RepoAccessorReportCreator()));

        AccumulatorWithAlgBlock pdfBlock = new AccumulatorWithAlgBlock<>(new PdfElementsAccumulator("report"),1 );
        pipe.add(pdfBlock);

        RepositoryAccessorBlock repoBlock = new RepositoryAccessorBlock<>(new LocalDirectoryRepository(),
                new RepoFileSaver(), "c:\\src\\reports");
        pipe.add(repoBlock);

        RepoRequest init = new RepoRequest(Paths.get("c:", "src","data").toString(), new LocalDirectoryRepository());

        pipe.run(init);
        System.out.println ("Pipeline finished");
    }

}
