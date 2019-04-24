package com.mirf.features.terminal

import com.mirf.core.GlobalConfig
import com.mirf.core.data.MirfException
import com.mirf.core.repository.LinkType
import com.mirf.features.repository.LocalRepositoryCommander
import java.io.File
import java.util.concurrent.TimeUnit

open class TerminalPlugin(val pluginDir: String, val commandMockup: String) {

    protected open val workingRepo: LocalRepositoryCommander = LocalRepositoryCommander(GlobalConfig.get("workingDir"))

    fun run() {}

    companion object {
        const val INPUT_FILE: String = "{%if}"
        const val INPUT_DIR: String = "{%id}"
        const val OUTPUT_FILE: String = "{%of}"
        const val OUTPUT_DIR: String = "{%od}"

        const val EXECUTION_TIMEOUT_SEC: Long = 300L
    }

    protected fun getOutputType(commandMockup: String): LinkType {
        if (commandMockup.contains(OUTPUT_DIR))
            return LinkType.Directory
        if (commandMockup.contains(OUTPUT_FILE))
            return LinkType.File
        throw MirfException("[Terminal plugin] failed to determine output type. Please, check commandWithArgs mockup")
    }

    protected fun buildCommand(commandMockup: String, input: String, output: String): String {
        var result = commandMockup.replace(INPUT_DIR, input)
        result = result.replace(INPUT_FILE, input)
        result = result.replace(OUTPUT_FILE, output)
        result = result.replace(OUTPUT_DIR, output)
        return result
    }

    protected fun String.runCommand(workingDir: String = ".",
                                    timeoutAmount: Long = 60,
                                    timeoutUnit: TimeUnit = TimeUnit.SECONDS): String? {
        val directory = File(workingDir)
        return ProcessBuilder(*this.split(" ".toRegex()).toTypedArray())
                .directory(directory)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start().apply {
                    waitFor(timeoutAmount, timeoutUnit)
                }.inputStream.bufferedReader().readText()
    }
}