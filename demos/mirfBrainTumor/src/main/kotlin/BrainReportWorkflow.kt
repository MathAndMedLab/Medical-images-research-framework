import com.mirf.core.data.Data
import com.mirf.core.data.FileData
import com.mirf.core.data.MirfData
import com.mirf.core.data.attribute.MirfAttributes
import com.mirf.core.data.attribute.Switch
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.pipeline.AlgorithmHostBlock
import com.mirf.core.pipeline.PipeStarter
import com.mirf.core.pipeline.Pipeline
import com.mirf.features.ij.asImageSeries
import com.mirf.features.nifti.util.Nifti1Reader
import com.mirf.features.repository.LocalRepositoryCommander
import com.mirf.features.repositoryaccessors.RepoFileSaver
import com.mirf.features.repositoryaccessors.RepositoryAccessorBlock
import pdfLayouts.PatientInfo
import java.nio.file.Paths
import java.time.LocalDateTime

class BrainReportWorkflow(
    private val t1Path: String, private val flairPath: String, private val workindDir: String,
    private val patientInfo: PatientInfo) {

    val pipe: Pipeline

    init {
        pipe = Pipeline("pipe", LocalDateTime.now(), LocalRepositoryCommander(Paths.get(workindDir)))

        //T1
        val t1Reader = AlgorithmHostBlock<Data, ImageSeries>(
            { val imgs = Nifti1Reader.read(t1Path).asImageSeries()
                imgs.attributes.add(MirfAttributes.THRESHOLDED.new(Switch.get()))
                return@AlgorithmHostBlock imgs},
            pipelineKeeper = pipe,
            name = "T1 reader"
        )

        val maskReader = AlgorithmHostBlock<Data, ImageSeries>(
            { val masks = Nifti1Reader.read(flairPath).asImageSeries()
                masks.attributes.add(MirfAttributes.THRESHOLDED.new(Switch.get()))
                return@AlgorithmHostBlock masks
            },
            pipelineKeeper = pipe,
            name = "FLAIR reader"
        )

        val reportBuilderBlock = ReportBuilderBlock(
            patientInfo = patientInfo,
            pipelineKeeper = pipe
        )

        val reportSaverBlock = RepositoryAccessorBlock<FileData, Data>(
            pipe.repositoryCommander,
            RepoFileSaver(), ""
        )

        reportBuilderBlock.setMasks(maskReader)
        reportBuilderBlock.setSeries(t1Reader)

        reportBuilderBlock.dataReady += reportSaverBlock::inputReady

        val root = PipeStarter()
        root.dataReady += t1Reader::inputReady
        root.dataReady += maskReader::inputReady
        pipe.rootBlock = root
    }

    fun exec() {
       pipe.run(MirfData.empty)
    }

}