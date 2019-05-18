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
import com.mirf.features.numinfofromimage.getVolume
import com.mirf.features.repository.LocalRepositoryCommander
import com.mirf.features.repositoryaccessors.RepoFileSaver
import com.mirf.features.repositoryaccessors.RepositoryAccessorBlock
import pdfLayouts.MsVolumeInfo
import pdfLayouts.PatientInfo
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime

class MsReportWorkflow(val pipe: Pipeline) {

    fun exec() {
        pipe.run(MirfData.empty)
    }

    companion object {
        fun createFull(
                url: String,
                curT1Path: String, curFlairPath: String, workingDir: String,
                patientInfo: PatientInfo,
                prevT1Path: String, prevFlairPath: String): MsReportWorkflow {

            val workingDirPath = Paths.get(workingDir).resolve("${patientInfo.name}_${LocalDate.now()}".replace(" ", "_"))
            val pipe = Pipeline("pipe", LocalDateTime.now(), LocalRepositoryCommander(workingDirPath))

            //current series
            val curT1Reader = AlgorithmHostBlock<Data, ImageSeries>({ Nifti1Reader.read(curT1Path).asImageSeries() },
                    pipelineKeeper = pipe, name = "FollowupT1")
            val curFlairReader = AlgorithmHostBlock<Data, ImageSeries>({ Nifti1Reader.read(curFlairPath).asImageSeries() },
                    pipelineKeeper = pipe, name = "FollowupFLAIR")
            val currentLesion = LesionSegmentation(url = url, pipelineKeeper = pipe)

            //prev series
            val prevT1Reader = AlgorithmHostBlock<Data, ImageSeries>({ Nifti1Reader.read(prevT1Path).asImageSeries() },
                    pipelineKeeper = pipe, name = "BaselineT1")
            val prevFlairReader = AlgorithmHostBlock<Data, ImageSeries>({ Nifti1Reader.read(prevFlairPath).asImageSeries() },
                    pipelineKeeper = pipe, name = "BaselineFLAIR")
            val prevLesion = LesionSegmentation(url = url, pipelineKeeper = pipe)
            val prevVolumeBlock = AlgorithmHostBlock<ImageSeries, MsVolumeInfo>({ MsVolumeInfo(it.getVolume(), VolumeValue.zero) },
                    pipelineKeeper = pipe, name = "Baseline volume calculator")
            prevLesion.setT1Sender(prevT1Reader)
            prevLesion.setFlairSender(prevFlairReader)
            prevLesion.dataReady += prevVolumeBlock::inputReady

            val reportBuilderBlock = ReportBuilderBlock(patientInfo = patientInfo, pipelineKeeper = pipe)
            val reportSaverBlock = RepositoryAccessorBlock<FileData, Data>(pipe.repositoryCommander, RepoFileSaver(), "")

            currentLesion.setFlairSender(curFlairReader)
            currentLesion.setT1Sender(curT1Reader)

            reportBuilderBlock.setMasks(currentLesion)
            reportBuilderBlock.setSeries(curT1Reader)
            reportBuilderBlock.setBaselineVolue(prevVolumeBlock)

            reportBuilderBlock.dataReady += reportSaverBlock::inputReady

            val root = PipeStarter()
            root.dataReady += curT1Reader::inputReady
            root.dataReady += curFlairReader::inputReady
            root.dataReady += prevFlairReader::inputReady
            root.dataReady += prevT1Reader::inputReady

            pipe.rootBlock = root
            return MsReportWorkflow(pipe)
        }
    }

}