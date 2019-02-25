package playground

import core.algorithm.asImageSeriesAlg
import core.data.Data
import core.data.FileData
import core.data.medimage.ImageSeriesData
import core.pipeline.AccumulatorWithAlgBlock
import core.pipeline.AlgorithmHostBlock
import core.pipeline.Pipeline
import features.dicomimage.alg.ImageTransformMode
import features.dicomimage.alg.SegmentationMaskApplicator
import features.dicomimage.util.DicomRepoRequestProcessors
import features.reports.PdfElementData
import features.reports.pdf.PdfElementsAccumulator
import features.reports.pdf.asPdfElementData
import features.repository.LocalRepositoryCommander
import features.repositoryaccessors.RepoFileSaver
import features.repositoryaccessors.RepositoryAccessorBlock
import features.repositoryaccessors.data.RepoRequest

class DicomImageCircleMaskApplier {

    fun exec(){
        //Creating pipeline
        val pipe = Pipeline("apply circle mask to dicom")

        //initializing blocks
        val seriesReaderBlock = AlgorithmHostBlock(
                DicomRepoRequestProcessors.ReadDicomImageSeriesAlg,
                pipelineKeeper = pipe)

        val addMaskBlock = AlgorithmHostBlock(
                AddCircleMaskAlg().asImageSeriesAlg(),
                pipelineKeeper = pipe)

        val applyMaskBlock = AlgorithmHostBlock(
                SegmentationMaskApplicator(ImageTransformMode.GenerateNew).asImageSeriesAlg(true),
                pipelineKeeper = pipe)

        val imageBeforeReporter = AlgorithmHostBlock<ImageSeriesData, PdfElementData>(
                {x -> x.asPdfElementData()},
                "image before", pipe)

        val imageAfterReporter = AlgorithmHostBlock<ImageSeriesData, PdfElementData>(
                {x -> x.asPdfElementData()},
                "image after", pipe)

        val pdfBlock = AccumulatorWithAlgBlock(PdfElementsAccumulator(
                "report"),
                2,
                "Accumulator",
                pipe)

        val reportSaverBlock = RepositoryAccessorBlock<FileData, Data>(LocalRepositoryCommander(),
                RepoFileSaver(), "c:\\src\\reports")

        //making connections
        seriesReaderBlock.dataReady += addMaskBlock::inputReady
        seriesReaderBlock.dataReady += imageBeforeReporter::inputReady

        addMaskBlock.dataReady += applyMaskBlock::inputReady
        applyMaskBlock.dataReady += imageAfterReporter::inputReady

        imageBeforeReporter.dataReady += pdfBlock::inputReady
        imageAfterReporter.dataReady += pdfBlock::inputReady

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


