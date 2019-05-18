package pdfLayouts

import com.mirf.core.data.Data
import com.mirf.core.data.FileData
import com.mirf.core.data.MirfData
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.pipeline.AccumulatorWithAlgBlock
import com.mirf.core.pipeline.AlgorithmHostBlock
import com.mirf.core.pipeline.PipeStarter
import com.mirf.core.pipeline.Pipeline
import com.mirf.features.elastix.ElastixBlock
import com.mirf.features.ij.asImageSeries
import com.mirf.features.nifti.util.Nifti1Reader
import com.mirf.features.pdf.PdfElementsAccumulator
import com.mirf.features.pdf.asPdfElementData
import com.mirf.features.reports.PdfElementData
import com.mirf.features.repository.LocalRepositoryCommander
import com.mirf.features.repositoryaccessors.RepoFileSaver
import com.mirf.features.repositoryaccessors.RepositoryAccessorBlock

/**
 * Predefined pipeline for multiple sclerosis report generation. Report is generated based on the 2 image series
 */
class MsPipeline {

    fun exec(baselineImageSeriesPath: String, followupImageSeriesPath: String, resultFolderLink: String) {
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
        val pdfBlock = AccumulatorWithAlgBlock(
                PdfElementsAccumulator(
                        "report"),
                2,
                "ReportCreator",
                pipe)

        val reportSaver = RepositoryAccessorBlock<FileData, Data>(
                LocalRepositoryCommander(),
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