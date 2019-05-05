package com.mirf.playground

import com.mirf.core.data.Data
import com.mirf.core.data.FileData
import com.mirf.core.data.MirfData
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.pipeline.AccumulatorWithAlgBlock
import com.mirf.core.pipeline.AlgorithmHostBlock
import com.mirf.core.pipeline.PipeStarter
import com.mirf.core.pipeline.Pipeline
import com.mirf.features.ij.asImageSeries
import com.mirf.features.mhdraw.MhdFile
import com.mirf.features.nifti.util.Nifti1Reader
import com.mirf.features.reports.PdfElementData
import com.mirf.features.pdf.PdfElementsAccumulator
import com.mirf.features.pdf.asPdfElementData
import com.mirf.features.repository.LocalRepositoryCommander
import com.mirf.features.repositoryaccessors.RepoFileSaver
import com.mirf.features.repositoryaccessors.RepositoryAccessorBlock

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