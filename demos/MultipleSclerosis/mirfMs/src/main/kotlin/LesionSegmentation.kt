import com.mirf.core.data.attribute.MirfAttributes
import com.mirf.core.data.attribute.Switch
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.core.pipeline.PipelineBlock
import com.mirf.core.pipeline.PipelineKeeper
import com.mirf.features.ij.asImageSeries
import com.mirf.features.nifti.util.Nifti1Reader
import khttp.extensions.fileLike
import remoteLesion.ProcessRequestStatus
import java.nio.file.Path
import java.nio.file.Paths

class LesionSegmentation constructor(val url: String, pipelineKeeper: PipelineKeeper) : PipelineBlock<ImageSeries, ImageSeries>("Lesion", pipelineKeeper) {


    private var t1Sender: Any? = null
    private var flairSender: Any? = null
    private var t1Set = false
    private var flairSet = false

    private var t1Series: ImageSeries? = null
    private var flairSeries: ImageSeries? = null


    override fun flush() {
        flushT1()
        flushFlair()
    }

    private fun flushFlair() {
        flairSender = null
        flairSet = false
        flairSeries = null
    }

    private fun flushT1() {
        t1Set = false
        t1Series = null
        t1Sender = null
    }

    override fun inputReady(sender: Any, input: ImageSeries) {

        when (sender) {
            t1Sender -> {
                t1Series = input
                t1Set = true
            }
            flairSender -> {
                flairSeries = input
                flairSet = true
            }
            else -> log.warn("[Lesion] undefined sender signal received from $sender, ignored")
        }

        if (t1Set && flairSet) {

            val record = pipelineKeeper.session.addNew("[$name]: algorithm execution")

            val file = createPostFile()
            val id = khttp.post(url = "$url/lesion",
                    files = listOf(file.fileLike("file")),
                    params = mapOf("mirf-ext" to ".tar.gz")).text

            waitUntilProcessed(id)

            val response = invokeWithRetry { khttp.get(url = "$url/lesion", params = mapOf("requestId" to id)) }
            val commander = pipelineKeeper.getRepositoryCommander(this)
            val resultLink = commander.saveFile(response.content.uncompressGzipArray(), "", "output.nii")

            val result = Nifti1Reader.read(commander.getAbsolutePath(resultLink)).asImageSeries()
            result.attributes.add(MirfAttributes.THRESHOLDED.new(Unit))

            record.setSuccess()

            onDataReady.invoke(this, result)
        }
    }

    private fun waitUntilProcessed(id: String) {

        val numOfRetry = 300000
        val interval = 4000

        for (i in 0..numOfRetry) {
            val response = khttp.get(
                    url = "$url/status",
                    params = mapOf("requestId" to id)
            )
            val status = ProcessRequestStatus.valueOf(response.text)
            if (status == ProcessRequestStatus.Processed)
                break
            Thread.sleep(interval.toLong())
        }
    }

    private fun createPostFile(): Path {
        val commander = pipelineKeeper.getRepositoryCommander(this)

        val t1Copy = Paths.get(t1Series!!.dumpToRepository(commander, "T1"))
        val flairCopy = Paths.get(flairSeries!!.dumpToRepository(commander, "FLAIR"))

        val archive = ArchiveCreator.createTar(true, commander.workingDir, t1Copy.toFile().name, flairCopy.toFile().name)
        return archive
    }

    fun setT1Sender(sender: PipelineBlock<*, ImageSeries>) {
        flushT1()
        this.t1Sender = sender
        sender.dataReady += this::inputReady
    }

    fun setFlairSender(sender: PipelineBlock<*, ImageSeries>) {
        flushFlair()
        this.flairSender = sender
        sender.dataReady += this::inputReady
    }

}