package playground

import core.data.Data
import core.data.FileData
import core.pipeline.impl.AccumulatorWithAlgBlock
import core.pipeline.impl.PipelineNode
import features.dicomimage.util.DicomRepoRequestProcessors
import features.reports.PdfElementData
import features.reports.creators.RepoAccessorReportCreator
import features.reports.pdf.DataTableReportToPdfElementConverter
import features.reports.pdf.ImageSeriesToPdfElementConverter
import features.reports.pdf.PdfElementsAccumulator
import features.repository.LocalRepositoryCommander
import features.repositoryaccessors.RepoFileSaver
import features.repositoryaccessors.RepositoryAccessorBlock
import features.repositoryaccessors.data.RepoRequest

object PdfImageCustomPipeline {

    fun exec() {
        val rootNode = PipelineNode(DicomRepoRequestProcessors.ReadDicomImageSeriesAlg)
        val tableReporter = rootNode.addListener(DataTableReportToPdfElementConverter(RepoAccessorReportCreator()))
        val imageReporter = rootNode.addListener(ImageSeriesToPdfElementConverter())

        val pdfBlock = AccumulatorWithAlgBlock<PdfElementData, FileData>(PdfElementsAccumulator("report"), 2)

        tableReporter.addListener(pdfBlock)
        imageReporter.addListener(pdfBlock)

        val repoBlock = RepositoryAccessorBlock<FileData, Data>(LocalRepositoryCommander(),
                RepoFileSaver(), "c:\\src\\reports")

        pdfBlock.addListener(repoBlock)

        val init = RepoRequest("c:\\src\\dicoms", LocalRepositoryCommander())

        rootNode.run(init)
    }
}
