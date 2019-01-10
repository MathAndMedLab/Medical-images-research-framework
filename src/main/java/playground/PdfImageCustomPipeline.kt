package playground

import core.data.Data
import core.data.FileData
import core.data.medimage.ImageSeriesData
import core.pipeline.AccumulatorWithAlgBlock
import core.pipeline.AlgorithmHostBlock
import features.dicomimage.util.DicomRepoRequestProcessors
import core.pipeline.Pipeline
import features.reports.PdfElementData
import features.reports.creators.RepoAccessorReportCreator
import features.reports.pdf.PdfElementsAccumulator
import features.reports.pdf.asPdfElementData
import features.repository.LocalRepositoryCommander
import features.repositoryaccessors.RepoFileSaver
import features.repositoryaccessors.RepositoryAccessorBlock
import features.repositoryaccessors.data.RepoRequest

object PdfImageCustomPipeline {

    fun exec() {

        //Creating pipeline
        val pipe = Pipeline("pdf to dicom")

        //initializing blocks
        val seriesReaderBlock = AlgorithmHostBlock(
                DicomRepoRequestProcessors.ReadDicomImageSeriesAlg,
                pipelineKeeper = pipe)

        val tableReportBlock = AlgorithmHostBlock<ImageSeriesData, PdfElementData>(
                { x -> RepoAccessorReportCreator().execute(x).asPdfElementData() },
                "Table reporter", pipe)

        val imageReporter = AlgorithmHostBlock<ImageSeriesData, PdfElementData>(
                {x -> x.asPdfElementData()},
                "image reporter", pipe)

        val pdfBlock = AccumulatorWithAlgBlock(PdfElementsAccumulator(
                "report"),
                2,
                "Accumulator",
                pipe)

        val reportSaverBlock = RepositoryAccessorBlock<FileData, Data>(LocalRepositoryCommander(),
                RepoFileSaver(), "c:\\src\\reports")

        //making connections
        seriesReaderBlock.dataReady += tableReportBlock::inputReady
        seriesReaderBlock.dataReady += imageReporter::inputReady

        tableReportBlock.dataReady += pdfBlock::inputReady
        imageReporter.dataReady += pdfBlock::inputReady

        pdfBlock.dataReady += reportSaverBlock::inputReady

        //create initial data
        val init = RepoRequest("c:\\src\\dicoms", LocalRepositoryCommander())

        //print every new session record
        pipe.session.newRecord += { _, b -> println(b) }

        //initialize root block and run
        pipe.rootBlock = seriesReaderBlock
        pipe.run(init)
    }
}
