package playground

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.UnitValue
import core.algorithm.asImageSeriesAlg
import core.data.Data
import core.data.FileData
import core.data.medimage.ImageSeries
import core.data.medimage.getImageWithHighlightedSegmentation
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
import features.repositoryaccessors.AlgorithmExecutionException
import features.repositoryaccessors.RepoFileSaver
import features.repositoryaccessors.RepositoryAccessorBlock
import features.repositoryaccessors.data.RepoRequest
import javax.imageio.ImageIO

class DicomImageCircleMaskApplier {

    fun exec() {
        //Creating pipeline
        val pipe = Pipeline("apply circle mask to dicom")

        //initializing blocks
        val seriesReaderBlock = AlgorithmHostBlock(
                DicomRepoRequestProcessors.readDicomImageSeriesAlg,
                pipelineKeeper = pipe)

        val addMaskBlock = AlgorithmHostBlock(
                AddCircleMaskAlg().asImageSeriesAlg(),
                pipelineKeeper = pipe)

        val applyMaskBlock = AlgorithmHostBlock(
                SegmentationMaskApplicator(ImageTransformMode.GenerateNew).asImageSeriesAlg(true),
                pipelineKeeper = pipe)

        val imageBeforeReporter = AlgorithmHostBlock<ImageSeries, PdfElementData>(
                { x -> x.asPdfElementData() },
                "image before", pipe)

        val imageAfterReporter = AlgorithmHostBlock<ImageSeries, PdfElementData>(
                { x -> createHighlightedImages(x) },
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

        addMaskBlock.dataReady += imageAfterReporter::inputReady

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

    fun createHighlightedImages(series: ImageSeries): PdfElementData {
        val images = series.images.map { x -> x.getImageWithHighlightedSegmentation() }

        val result = Paragraph()
        try {
            for (image in images) {
                val stream = ByteArrayOutputStream()
                ImageIO.write(image, "jpg", stream)

                val pdfImage = Image(ImageDataFactory.create(stream.toByteArray()))
                pdfImage.width = UnitValue.createPercentValue(50f)
                pdfImage.setMargins(10f, 10f, 10f, 10f)
                pdfImage.setHeight(UnitValue.createPercentValue(50f))

                result.add(pdfImage)
            }
        } catch (e: Exception) {
            throw AlgorithmExecutionException(e)
        }

        return PdfElementData(result)
    }
}


