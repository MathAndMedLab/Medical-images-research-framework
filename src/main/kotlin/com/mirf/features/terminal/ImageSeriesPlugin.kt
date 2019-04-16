package com.mirf.features.terminal

import com.mirf.core.data.medimage.ImageSeries
import java.util.concurrent.TimeUnit

class ImageSeriesPlugin(pluginDir: String, commandMockup: String) : TerminalPlugin(pluginDir, commandMockup) {

    private var output: String? = null

    fun runPlugin(imageSeries: ImageSeries) {
        val input = imageSeries.dumpToRepository(workingRepo)
        val outType = getOutputType(commandMockup)
        output = workingRepo.generateLink(outType)
        val command = buildCommand(commandMockup, input, output!!)

        val result = command.runCommand(pluginDir, EXECUTION_TIMEOUT_SEC, TimeUnit.SECONDS)
    }

    fun getResultAsIMageSeries(): ImageSeries {
        TODO()
    }
}