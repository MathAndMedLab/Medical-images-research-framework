package playground

import core.data.Data
import core.data.FileData
import core.data.MirfData
import core.data.medimage.ImageSeries
import core.pipeline.AccumulatorWithAlgBlock
import core.pipeline.AlgorithmHostBlock
import core.pipeline.PipeStarter
import core.pipeline.Pipeline
import features.ij.asImageSeries
import features.mhdraw.MhdFile
import features.nifti.util.Nifti1Reader
import features.reports.PdfElementData
import features.reports.pdf.PdfElementsAccumulator
import features.reports.pdf.asPdfElementData
import features.repository.LocalRepositoryCommander
import features.repositoryaccessors.RepoFileSaver
import features.repositoryaccessors.RepositoryAccessorBlock

class NiftiTest {
    fun exec(niftiFileLink: String, mhdFileLink: String, resultFolderLink: String) {

        val pipe = Pipeline("apply circle mask to dicom")

        //initializing blocks
        val niftiReader = AlgorithmHostBlock<Data, ImageSeries>(
                { Nifti1Reader.read(niftiFileLink).asImageSeries() },
                pipelineKeeper = pipe)

        val rawReader = AlgorithmHostBlock<Data, ImageSeries>(
                { MhdFile.load(mhdFileLink).image.asImageSeries() },
                pipelineKeeper = pipe)

        val niftiToPdf = AlgorithmHostBlock<ImageSeries, PdfElementData>(
                { it.asPdfElementData(40..50 step 4) },
                "image after", pipe)

        val rawToPdf = AlgorithmHostBlock<ImageSeries, PdfElementData>(
                { it.asPdfElementData(40..50 step 4) },
                "image after", pipe)

        val pdfBlock = AccumulatorWithAlgBlock(PdfElementsAccumulator(
                "report"),
                2,
                "Accumulator",
                pipe)

        val reportSaverBlock = RepositoryAccessorBlock<FileData, Data>(LocalRepositoryCommander(),
                RepoFileSaver(), resultFolderLink)

        //making connections
        niftiReader.dataReady += niftiToPdf::inputReady
        rawReader.dataReady += rawToPdf::inputReady

        niftiToPdf.dataReady += pdfBlock::inputReady
        rawToPdf.dataReady += pdfBlock::inputReady

        pdfBlock.dataReady += reportSaverBlock::inputReady

        //create initial data
        //print every new session record
        pipe.session.newRecord += { _, b -> println(b) }

        //run
        val root = PipeStarter()
        root.dataReady += niftiReader::inputReady
        root.dataReady += rawReader::inputReady

        pipe.rootBlock = root
        pipe.run(MirfData.empty)
    }
}