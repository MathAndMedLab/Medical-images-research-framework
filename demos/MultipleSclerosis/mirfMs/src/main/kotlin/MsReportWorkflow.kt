import com.mirf.core.common.VolumeValue
import com.mirf.core.data.Data
import com.mirf.core.data.FileData
import com.mirf.core.data.MirfData
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.pipeline.AlgorithmHostBlock
import com.mirf.core.pipeline.PipeStarter
import com.mirf.core.pipeline.Pipeline
import com.mirf.features.ij.asImageSeries
import com.mirf.features.nifti.util.Nifti1Reader
import com.mirf.features.repository.LocalRepositoryCommander
import com.mirf.features.repositoryaccessors.RepoFileSaver
import com.mirf.features.repositoryaccessors.RepositoryAccessorBlock
import pdfLayouts.MsVolumeInfo
import pdfLayouts.PatientInfo
import java.nio.file.Paths
import java.time.LocalDateTime

class MsReportWorkflow(
        private val url: String,
        private val t1Path: String, private val flairPath: String, private val workindDir: String,
        private val patientInfo: PatientInfo, private val prevVolumeInfo: MsVolumeInfo) {

    val pipe: Pipeline

    init {
        val workingDirPath = Paths.get(workindDir)
        pipe = Pipeline("pipe", LocalDateTime.now(), LocalRepositoryCommander(workingDirPath))

        //T1
        val t1Reader = AlgorithmHostBlock<Data, ImageSeries>(
                { Nifti1Reader.read(t1Path).asImageSeries() },
                pipelineKeeper = pipe,
                name = "T1 reader"
        )

        //FLAIR
        val flairReader = AlgorithmHostBlock<Data, ImageSeries>(
                { Nifti1Reader.read(flairPath).asImageSeries() },
                pipelineKeeper = pipe,
                name = "FLAIR reader"
        )

        val lesionSegmentationBlock = LesionSegmentation(
                url = url,
                pipelineKeeper = pipe
        )

        val reportBuilderBlock = ReportBuilderBlock(
                patientInfo = patientInfo,
                prevVolumeInfo = prevVolumeInfo,
                pipelineKeeper = pipe
        )

        val reportSaverBlock = RepositoryAccessorBlock<FileData, Data>(
                pipe.repositoryCommander,
                RepoFileSaver(), ""
        )

        lesionSegmentationBlock.setFlairSender(flairReader)
        lesionSegmentationBlock.setT1Sender(t1Reader)

        reportBuilderBlock.setMasks(lesionSegmentationBlock)
        reportBuilderBlock.setSeries(t1Reader)

        reportBuilderBlock.dataReady += reportSaverBlock::inputReady

        val root = PipeStarter()
        root.dataReady += t1Reader::inputReady
        root.dataReady += flairReader::inputReady
        pipe.rootBlock = root
    }

    fun exec() {
        pipe.run(MirfData.empty)
    }

}