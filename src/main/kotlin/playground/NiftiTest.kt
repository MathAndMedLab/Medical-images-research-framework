package playground

import core.data.Data
import core.data.FileData
import core.data.MirfData
import core.data.medimage.ImageSeries
import core.pipeline.AccumulatorWithAlgBlock
import core.pipeline.AlgorithmHostBlock
import core.pipeline.Pipeline
import features.nifti.util.Nifti1Reader
import features.reports.PdfElementData
import features.reports.pdf.PdfElementsAccumulator
import features.reports.pdf.asPdfElementData
import features.repository.LocalRepositoryCommander
import features.repositoryaccessors.RepoFileSaver
import features.repositoryaccessors.RepositoryAccessorBlock

class NiftiTest {
    fun exec(niftiFileLink: String, resultFolderLink: String) {

        val pipe = Pipeline("apply circle mask to dicom")

        //initializing blocks
        val niftiReader = AlgorithmHostBlock<Data, ImageSeries>(
                { Nifti1Reader.read(niftiFileLink) },
                pipelineKeeper = pipe)

        val imageToPdf = AlgorithmHostBlock<ImageSeries, PdfElementData>(
                { it.asPdfElementData(60..80) },
                "image after", pipe)

        val pdfBlock = AccumulatorWithAlgBlock(PdfElementsAccumulator(
                "report"),
                1,
                "Accumulator",
                pipe)

        val reportSaverBlock = RepositoryAccessorBlock<FileData, Data>(LocalRepositoryCommander(),
                RepoFileSaver(), resultFolderLink)

        //making connections
        niftiReader.dataReady += imageToPdf::inputReady

        imageToPdf.dataReady += pdfBlock::inputReady

        pdfBlock.dataReady += reportSaverBlock::inputReady

        //create initial data
        //print every new session record
        pipe.session.newRecord += { _, b -> println(b) }

        //run
        pipe.rootBlock = niftiReader
        pipe.run(MirfData.empty)
    }
}