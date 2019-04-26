import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.pipeline.PipelineBlock
import com.mirf.core.pipeline.PipelineKeeper
import com.mirf.features.ij.asImageSeries
import com.mirf.features.nifti.util.Nifti1Reader
import khttp.extensions.fileLike
import java.nio.file.Paths

class BrainExtractionHttpBlock constructor(private val url: String, pipelineKeeper: PipelineKeeper) :
    PipelineBlock<ImageSeries, ImageSeries>("brain extractor", pipelineKeeper) {

    override fun inputReady(sender: Any, input: ImageSeries) {

        val commander = pipelineKeeper.getRepositoryCommander(this)

        val path = input.dumpToRepository(commander)
        //val archive = ArchiveCreator.createTar(true, commander.workingDir, Paths.get(path))

        val id = khttp.post(url = url,
            files = listOf(Paths.get(path).fileLike("file")),
            params = mapOf("mirf-ext" to ".nii")).text

        val response = invokeWithRetry { khttp.get(url = url, params = mapOf("requestId" to id))}

        val resultLink = commander.saveFile(response.content.uncompressGzipArray(), "", "output.nii")

        val result = Nifti1Reader.read(commander.getAbsolutePath(resultLink)).asImageSeries()

        onDataReady.invoke(this, result)
    }

    override fun flush() {
    }
}