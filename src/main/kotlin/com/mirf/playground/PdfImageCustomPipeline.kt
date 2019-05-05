package com.mirf.playground

import com.mirf.core.data.Data
import com.mirf.core.data.FileData
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.pipeline.AccumulatorWithAlgBlock
import com.mirf.core.pipeline.AlgorithmHostBlock
import com.mirf.features.dicomimage.util.DicomRepoRequestProcessors
import com.mirf.core.pipeline.Pipeline
import com.mirf.features.reports.PdfElementData
import com.mirf.features.reports.creators.RepoAccessorReportCreator
import com.mirf.features.pdf.PdfElementsAccumulator
import com.mirf.features.pdf.asPdfElementData
import com.mirf.features.repository.LocalRepositoryCommander
import com.mirf.features.repositoryaccessors.RepoFileSaver
import com.mirf.features.repositoryaccessors.RepositoryAccessorBlock
import com.mirf.features.repositoryaccessors.data.RepoRequest

object PdfImageCustomPipeline {

    fun exec() {

        //Creating pipeline
        val pipe = Pipeline("pdf to dicom")

        //initializing blocks
        val seriesReaderBlock = AlgorithmHostBlock(
                DicomRepoRequestProcessors.readDicomImageSeriesAlg,
                pipelineKeeper = pipe)

        val tableReportBlock = AlgorithmHostBlock<ImageSeries, PdfElementData>(
                { x -> RepoAccessorReportCreator().execute(x).asPdfElementData() },
                "Table reporter", pipe)

        val imageReporter = AlgorithmHostBlock<ImageSeries, PdfElementData>(
                { x -> x.asPdfElementData() },
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
