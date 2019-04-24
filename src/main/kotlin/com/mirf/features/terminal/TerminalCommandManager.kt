package com.mirf.features.terminal

import com.mirf.core.GlobalConfig
import com.mirf.features.repository.LocalRepositoryCommander
import java.io.File
import java.util.concurrent.TimeUnit

open class TerminalCommandManager(var commandWithArgs: String,
                                  protected open val workingRepo: LocalRepositoryCommander = LocalRepositoryCommander(GlobalConfig.get("workingDir"))) {

    fun runSync(timeoutSeconds: Long = 60): String? {
        val directory = File(workingRepo.workingDir)
        return ProcessBuilder(*commandWithArgs.split(" ".toRegex()).toTypedArray())
                .directory(directory)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start().apply {
                    waitFor(timeoutSeconds, TimeUnit.SECONDS)
                }.inputStream.bufferedReader().readText()
    }
}