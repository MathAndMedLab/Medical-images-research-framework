package playground

import core.data.Data
import core.data.FileData
import core.pipeline.impl.AccumulatorWithAlgBlock
import core.pipeline.impl.PipelineNode
import features.dicomimage.util.DicomRepoRequestProcessors
import features.reports.creators.RepoAccessorReportCreator
import features.reports.pdf.PdfElementsAccumulator
import features.reports.pdf.asPdfElementData
import features.repository.LocalRepositoryCommander
import features.repositoryaccessors.RepoFileSaver
import features.repositoryaccessors.RepositoryAccessorBlock
import features.repositoryaccessors.data.RepoRequest

object PdfImageCustomPipeline {

    fun exec() {
        val rootNode = PipelineNode(DicomRepoRequestProcessors.ReadDicomImageSeriesAlg)
        val tableReporter = rootNode.addListener { x -> RepoAccessorReportCreator().execute(x).asPdfElementData()}
        val imageReporter = rootNode.addListener{ x -> x.asPdfElementData()}

        val pdfBlock = AccumulatorWithAlgBlock(PdfElementsAccumulator("report"), 2)

        tableReporter.addListener(pdfBlock)
        imageReporter.addListener(pdfBlock)

        val repoBlock = RepositoryAccessorBlock<FileData, Data>(LocalRepositoryCommander(),
                RepoFileSaver(), "c:\\src\\reports")

        pdfBlock.addListener(repoBlock)

        val init = RepoRequest("c:\\src\\dicoms", LocalRepositoryCommander())

        rootNode.run(init)
    }
}
