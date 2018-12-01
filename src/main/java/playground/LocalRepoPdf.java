package playground;

import core.pipeline.impl.AccumulatorWithAlgBlock;
import core.pipeline.impl.SequentialPipeline;
import features.reports.creators.RepoAccessorReportCreator;
import features.reports.pdf.MirfReportToPdfElementConverter;
import features.reports.pdf.PdfElementsAccumulator;
import features.repository.LocalRepositoryCommander;
import features.repositoryaccessors.RepoFileSaver;
import features.repositoryaccessors.RepoImageSeriesAccessor;
import features.repositoryaccessors.RepositoryAccessorBlock;
import features.repositoryaccessors.data.RepoRequest;

import java.nio.file.Paths;

public class LocalRepoPdf {
    public void exec()
    {
        SequentialPipeline pipe = new SequentialPipeline();

        pipe.add(new RepoImageSeriesAccessor());
        pipe.add(new MirfReportToPdfElementConverter<>(new RepoAccessorReportCreator()));

        AccumulatorWithAlgBlock pdfBlock = new AccumulatorWithAlgBlock<>(new PdfElementsAccumulator("report"),1 );
        pipe.add(pdfBlock);

        RepositoryAccessorBlock repoBlock = new RepositoryAccessorBlock<>(new LocalRepositoryCommander(),
                new RepoFileSaver(), "c:\\src\\reports");
        pipe.add(repoBlock);

        RepoRequest init = new RepoRequest(Paths.get("c:", "src","data").toString(), new LocalRepositoryCommander());

        pipe.run(init);
        System.out.println ("Pipeline finished");
    }

}
