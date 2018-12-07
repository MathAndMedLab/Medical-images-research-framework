package playground;

import core.pipeline.impl.AccumulatorWithAlgBlock;
import core.pipeline.impl.AlgorithmHostBlock;
import features.dicomimage.util.DicomRepoRequestProcessors;
import features.reports.creators.RepoAccessorReportCreator;
import features.reports.pdf.DataTableReportToPdfElementConverter;
import features.reports.pdf.ImageSeriesToPdfElementConverter;
import features.reports.pdf.PdfElementsAccumulator;
import features.repository.LocalRepositoryCommander;
import features.repositoryaccessors.RepoFileSaver;
import features.repositoryaccessors.RepositoryAccessorBlock;
import features.repositoryaccessors.data.RepoRequest;

public class PdfImageCustomPipeline {

    public static void exec()
    {
        AlgorithmHostBlock dicomReader = new AlgorithmHostBlock<>(DicomRepoRequestProcessors.ReadDicomImageSeriesAlg);
        AlgorithmHostBlock tableReporter = new AlgorithmHostBlock<>(new DataTableReportToPdfElementConverter<>(new RepoAccessorReportCreator()));
        AlgorithmHostBlock imageReporter = new AlgorithmHostBlock<>(new ImageSeriesToPdfElementConverter());

        dicomReader.addListener(tableReporter);
        dicomReader.addListener(imageReporter);

        AccumulatorWithAlgBlock pdfBlock = new AccumulatorWithAlgBlock<>(new PdfElementsAccumulator("report"),2 );

        tableReporter.addListener(pdfBlock);
        imageReporter.addListener(pdfBlock);

        RepositoryAccessorBlock repoBlock = new RepositoryAccessorBlock<>(new LocalRepositoryCommander(),
                new RepoFileSaver(), "c:\\src\\reports");

        pdfBlock.addListener(repoBlock);

        RepoRequest init = new RepoRequest("c:\\src\\dicoms", new LocalRepositoryCommander());

        dicomReader.inputDataReady(null, init);

        System.out.println ("Pipeline finished");
    }
}
