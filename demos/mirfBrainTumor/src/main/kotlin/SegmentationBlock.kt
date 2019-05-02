import com.mirf.core.data.Data
import com.mirf.core.data.MirfData
import com.mirf.core.pipeline.PipelineBlock
import com.mirf.core.pipeline.PipelineKeeper
import java.io.File
import java.util.concurrent.TimeUnit


class SegmentationBlock(val rootFolder: String,
                        pipelineKeeper: PipelineKeeper) : PipelineBlock<Data, Data>(
        "Segmentation block", pipelineKeeper) {

    override fun flush() {}

    /**
     * Shorthand for [File.execute]. Assumes that all spaces are argument separators,
     * so no argument may contain a space.
     * ```kotlin
     *  // Example
     *  directory exec "git status"
     *
     *  // This fails since `'A` and `message'` will be considered as two arguments
     *  directory exec "git commit -m 'A message'"
     * ```
     */
    infix fun File.exec(command: String): String {
        val arguments = command.split(' ').toTypedArray()
        return execute(*arguments)
    }

    /**
     * Executes command. Arguments may contain strings. More appropriate than [File.exec]
     * when using dynamic arguments.
     * ```kotlin
     *  // Example
     *  directory.execute("git", "commit", "-m", "A message")
     * ```
     */
    fun File.execute(vararg arguments: String): String {
        val process = ProcessBuilder(*arguments)
                .directory(this)
                .start()
                .also { it.waitFor(20, TimeUnit.MINUTES) }

        if (process.exitValue() != 0) {
            throw Exception(process.errorStream.bufferedReader().readText())
        }
        return process.inputStream.bufferedReader().readText()
    }

    override fun inputReady(sender: Any, input: Data) {
        val record = pipelineKeeper.session.addNew(
                "[$name]: segmentation algorithm execution")
        var classPath = this.javaClass.getProtectionDomain().getCodeSource().getLocation().getPath()
        var segmentation_pref = "../../../segmentation"
        var python_arg = rootFolder
        File(classPath + segmentation_pref).execute(
                "python3", "run_segmentation.py", python_arg)


        onDataReady(this, MirfData.empty)

        record.setSuccess()
    }
}
