package modules.ms

import core.data.Data
import core.data.FileData
import core.data.MirfData
import core.data.medimage.ImageSeries
import core.pipeline.AccumulatorWithAlgBlock
import core.pipeline.AlgorithmHostBlock
import core.pipeline.PipeStarter
import core.pipeline.Pipeline
import features.ij.asImageSeries
import features.nifti.util.Nifti1Reader
import features.reports.PdfElementData
import features.reports.pdf.PdfElementsAccumulator
import features.reports.pdf.asPdfElementData
import features.repository.LocalRepositoryCommander
import features.repositoryaccessors.RepoFileSaver
import features.repositoryaccessors.RepositoryAccessorBlock
import modules.elastix.ElastixBlock

/**
 * Predefined pipeline for multiple sclerosis report generation. Report is generated based on the 2 image series
 */
class MsPipeline {

    fun exec(baselineImageSeriesPath: String, followupImageSeriesPath: String, resultFolderLink: String)  {
        val pipe = Pipeline("MS report generator")

        //initializing blocks
        val baselineReader = AlgorithmHostBlock<Data, ImageSeries>(
                { Nifti1Reader.read(baselineImageSeriesPath).asImageSeries() },
                pipelineKeeper = pipe, name = "Baseline reader")

        val followupReader = AlgorithmHostBlock<Data, ImageSeries>(
                { Nifti1Reader.read(followupImageSeriesPath).asImageSeries() },
                pipelineKeeper = pipe, name = "Followup reader")

        val elastixBlock = ElastixBlock(pipelineKeeper = pipe)

        val beforeRegistration = AlgorithmHostBlock<ImageSeries, PdfElementData>(
                { it.asPdfElementData(40..50 step 4) },
                "image before", pipe)

        val afterRegistration = AlgorithmHostBlock<ImageSeries, PdfElementData>(
                { it.asPdfElementData(40..50 step 4) },
                "image after", pipe)

        //TODO:(avlomakin) create method for single PdfData to FileData transform
        val pdfBlock = AccumulatorWithAlgBlock(PdfElementsAccumulator(
                "report"),
                2,
                "ReportCreator",
                pipe)

        val reportSaver = RepositoryAccessorBlock<FileData, Data>(LocalRepositoryCommander(),
                RepoFileSaver(), resultFolderLink)

        elastixBlock.setFixedSender(baselineReader)
        elastixBlock.setMovingSender(followupReader)

        followupReader.dataReady += beforeRegistration::inputReady
        elastixBlock.dataReady += afterRegistration::inputReady

        beforeRegistration.dataReady += pdfBlock::inputReady
        afterRegistration.dataReady += pdfBlock::inputReady

        pdfBlock.dataReady += reportSaver::inputReady

        pipe.session.newRecord += { _, b -> println(b) }

        //run
        val root = PipeStarter()
        root.dataReady += baselineReader::inputReady
        root.dataReady += followupReader::inputReady

        pipe.rootBlock = root
        pipe.run(MirfData.empty)
    }
}