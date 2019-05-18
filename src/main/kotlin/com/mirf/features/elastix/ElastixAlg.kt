package com.mirf.features.elastix

import com.mirf.core.GlobalConfig
import com.mirf.core.algorithm.Algorithm
import com.mirf.core.data.medimage.ImageSeries
import com.mirf.features.ij.asImageSeries
import com.mirf.features.mhdraw.MhdFile
import com.mirf.features.repository.LocalRepositoryCommander
import com.mirf.features.terminal.TerminalCommandManager
import java.lang.Exception
import java.nio.file.Paths

class ElastixAlg : Algorithm<RegistrationInfo, ImageSeries> {

    private val logPrefix: String = "Elastix"

    override fun execute(input: RegistrationInfo): ImageSeries {

        val elastixDir = GlobalConfig.get<String>("elastix_dir")
        val commander = LocalRepositoryCommander(Paths.get(elastixDir))

        try {
            val movingPath = input.movingSeries.dumpToRepository(commander)
            val fixedPath = input.fixedSeries.dumpToRepository(commander)
            log.info("$logPrefix moving series - \'$movingPath\', fixed series - \'$fixedPath\'")

            val outCommander = commander.createRepoCommanderFor(this)
            val paramFile = outCommander.saveFile(input.params.toByteArray(), "", "param.txt")
            log.info("$logPrefix params file generated. Path - \'$paramFile\', \n ${input.params}")

            val command = "\"${Paths.get(elastixDir, "elastix")}\" -f \"$fixedPath\" -m \"$movingPath\" -out \"${outCommander.workingDir}\" -p \"${outCommander.getAbsolutePath(paramFile)}\""
            log.info("$logPrefix generated command - \'$command\'")
            log.info("$ logPrefix running command")
            val terminalManager = TerminalCommandManager(command)

            terminalManager.runSync()

            val resultPath = outCommander.getAbsolutePath("result.0.mhd")

            return MhdFile.load(resultPath).image.asImageSeries()
        } catch (e: Exception) {
            commander.cleanupSafe()
            throw e
        }
    }
}