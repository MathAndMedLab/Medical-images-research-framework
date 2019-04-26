import com.mirf.core.log.MirfLogFactory
import org.slf4j.Logger
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream

object ArchiveCreator {

    private val log: Logger = MirfLogFactory.currentLogger

    fun createTar(gzipped: Boolean = true, workingDir: Path, vararg filenames : String ) : Path{

        val files = filenames.joinToString(" ")
        val resultFileName = workingDir.resolve("${getName()}.tar${if (gzipped) ".gz" else ""}")

        val command = "tar -c${if (gzipped ) "z" else ""}f $resultFileName  $files"

        runSync(command, workingDir)

        return resultFileName
    }

    private fun getName() : String{
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        return LocalDateTime.now().format(formatter)
    }

    private fun simpleSyncRun(command: String) {
        val process = Runtime.getRuntime().exec(command)
        process.waitFor()
    }

    fun runSync(command: String, workingDir: Path): String? {
        val output=  ProcessBuilder(*command.split(" ".toRegex()).toTypedArray())
            .directory(workingDir.toFile())
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start().apply {
                waitFor(10, TimeUnit.MINUTES)
            }.inputStream.bufferedReader().readText()
        log.info("$command done, output: $output")
        return output
    }

}


fun ByteArray.uncompressGzipArray() : ByteArray{
    val bytein = ByteArrayInputStream(this)
    val gzin = GZIPInputStream(bytein)
    val byteout = ByteArrayOutputStream()

    var res = 0
    val buf = ByteArray(1024)
    while (res >= 0) {
        res = gzin.read(buf, 0, buf.size)
        if (res > 0) {
            byteout.write(buf, 0, res)
        }
    }
    return byteout.toByteArray()
}