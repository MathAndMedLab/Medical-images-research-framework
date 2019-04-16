package com.mirf.core.common

import com.mirf.core.log.MirfLogFactory
import org.slf4j.Logger
import java.io.File

class ExternalResourceLoader {

    private val log: Logger = MirfLogFactory.currentLogger

    fun loadExternalResources(rootDir: File) {

        val resourceDirs = rootDir.listFiles { a -> a.isDirectory }
        log.info("found ${resourceDirs.size} resource directories, enumerating through")

        for (resourceDir in resourceDirs) {
            log.info("searching for $RESOURCE_DESC_FILE_EXT file in ${resourceDir.name}")
            val descFiles = resourceDir.listFiles { file -> file.isFile && file.extension == RESOURCE_DESC_FILE_EXT }

            if (descFiles.size > 1) {
                log.warn("More than 1 config file found in ${resourceDir.name}, skipping loading")
                continue
            }
            if (descFiles.isEmpty()) {
                log.info("No config file found in ${resourceDir.name}, skipping loading")
                continue
            }

            log.info("external resource found in $resourceDir, loading...")

            val desc = descFiles[0]

            ExternalResource.load(desc)
        }
    }

    fun loadExternalResources() {
        loadExternalResources(EnvironmentHelper.getCurrentJarDirectory())
    }

    companion object {
        private const val RESOURCE_DESC_FILE_EXT = "mcfg"
    }

}