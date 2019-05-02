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
        private val dataPath: String,
        private val coreImagePath: String,
        private val workindDir: String,
        private val patientInfo: PatientInfo) {

    private val segmentationClassPath = this.javaClass.getProtectionDomain().getCodeSource().getLocation().getPath() + "../../../segmentation/result/"

    val pipe: Pipeline

    init {
        pipe = Pipeline("pipe", LocalDateTime.now(),
                LocalRepositoryCommander(Paths.get(workindDir)))

        // The main image to apply all masks on it
        val imageReader = AlgorithmHostBlock<Data, ImageSeries>(
                {
                    val imgs = Nifti1Reader.read(coreImagePath).asImageSeries()
                imgs.attributes.add(MirfAttributes.THRESHOLDED.new(Switch.get()))
                return@AlgorithmHostBlock imgs},
            pipelineKeeper = pipe,
            name = "T1 reader"
        )
        val wholeMaskReader = AlgorithmHostBlock<Data, ImageSeries>(
                {
                    val masks = Nifti1Reader.read(
                            segmentationClassPath + "segmentation_wh.nii.gz")
                            .asImageSeries()
                masks.attributes.add(MirfAttributes.THRESHOLDED.new(Switch.get()))
                return@AlgorithmHostBlock masks
            },
            pipelineKeeper = pipe,
            name = "Whole masks reader"
        )

        val segmentationBlock = SegmentationBlock(rootFolder = dataPath, pipelineKeeper = pipe)

        val coreMaskReader = AlgorithmHostBlock<Data, ImageSeries>(
                {
                    val masks = Nifti1Reader.read(
                            segmentationClassPath + "segmentation_core.nii.gz")
                            .asImageSeries()
                    masks.attributes.add(MirfAttributes.THRESHOLDED.new(Switch.get()))
                    return@AlgorithmHostBlock masks
                },
                pipelineKeeper = pipe,
                name = "Core masks reader"
        )

        val edemaMaskReader = AlgorithmHostBlock<Data, ImageSeries>(
                {
                    val masks = Nifti1Reader.read(
                            segmentationClassPath + "segmentation_ench.nii.gz")
                            .asImageSeries()
                    masks.attributes.add(MirfAttributes.THRESHOLDED.new(Switch.get()))
                    return@AlgorithmHostBlock masks
                },
                pipelineKeeper = pipe,
                name = "Edema masks reader"
        )

        val reportBuilderBlock = ReportBuilderBlock(
            patientInfo = patientInfo,
            pipelineKeeper = pipe
        )

        val reportSaverBlock = RepositoryAccessorBlock<FileData, Data>(
            pipe.repositoryCommander,
            RepoFileSaver(), ""
        )

        reportBuilderBlock.setMasks(wholeMaskReader)
        reportBuilderBlock.setCoreMasks(coreMaskReader)
        reportBuilderBlock.setEdemaMasks(edemaMaskReader)
        reportBuilderBlock.setSeries(imageReader)

        reportBuilderBlock.dataReady += reportSaverBlock::inputReady

        val root = PipeStarter()
        root.dataReady += segmentationBlock::inputReady
        segmentationBlock.dataReady += imageReader::inputReady
        segmentationBlock.dataReady += wholeMaskReader::inputReady
        segmentationBlock.dataReady += coreMaskReader::inputReady
        segmentationBlock.dataReady += edemaMaskReader::inputReady
        pipe.rootBlock = root
    }

    fun exec() {
       pipe.run(MirfData.empty)
    }

}