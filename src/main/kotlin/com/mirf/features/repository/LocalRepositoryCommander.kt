package com.mirf.features.repository

import com.mirf.core.log.MirfLogFactory
import com.mirf.core.pipeline.Pipeline
import com.mirf.core.pipeline.PipelineBlock
import com.mirf.core.repository.LinkType
import com.mirf.core.repository.RepositoryCommander
import com.mirf.core.repository.RepositoryCommanderException
import org.slf4j.Logger

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashSet

/**
 * Local file system commander, Link = path on a filesystem
 */
class LocalRepositoryCommander constructor(val workingDir: Path = Paths.get("")) : RepositoryCommander {

    private val log: Logger = MirfLogFactory.currentLogger
    private val createdSubCommanders: ConcurrentHashMap<Any, LocalRepositoryCommander> = ConcurrentHashMap()

    init {
        if (!Files.exists(workingDir)) {
            log.info("creating $workingDir")
            Files.createDirectory(workingDir)
        } else {
            log.info("directory $workingDir already exists")
        }
    }

    private val tempFiles = HashSet<Path>()

    override fun generateLink(type: LinkType): String {
        return UUID.randomUUID().toString()
    }

    @Throws(RepositoryCommanderException::class)
    override fun getFile(link: String): ByteArray {
        val filePath = workingDir.resolve(link)
        try {
            return Files.readAllBytes(filePath)
        } catch (e: IOException) {
            throw RepositoryCommanderException("Failed to read file bytes", e)
        }
    }

    override fun toString(): String {
        return workingDir.toString()
    }

    override fun getSeriesFileLinks(link: String): Array<String> {
        val pathUri = workingDir.resolve(link).toUri()
        return File(pathUri).listFiles().filter { it.isFile }.map { it.path }.toTypedArray()
    }

    @Throws(RepositoryCommanderException::class)
    override fun saveFile(file: ByteArray, link: String, filename: String): String {
        try {
            val streamLink = workingDir.resolve(link).resolve(filename).toString()
            val stream = FileOutputStream(streamLink)
            stream.write(file)
            return Paths.get(link, filename).toString()
        } catch (e: IOException) {
            throw RepositoryCommanderException("Failed to write file bytes", e)
        }
    }

    /**
     * Creates temp directory
     * @return relative directory path
     */
    fun createSubDir(): Path {
        val path = workingDir.resolve(UUID.randomUUID().toString())
        Files.createDirectory(path)

        tempFiles.add(path)
        return path
    }

    fun createSubDir(prefix: String) : Path{

        val path = workingDir.resolve(prefix + "_" + UUID.randomUUID().toString().replace(Regex("-.*"), ""))
        Files.createDirectory(path)

        tempFiles.add(path)
        return path
    }

    /**
     * Removes all temp directories and files from the file system.
     * If any of the file is in use, it will be skipped
     */
    fun cleanupSafe() {
        for (path in tempFiles) {
            try {
                Files.delete(path)
            } catch (e: Exception) {
            }
        }
    }

    override fun createRepoCommanderFor(entity: Any): LocalRepositoryCommander {

        val commander = LocalRepositoryCommander(createSubDir())
        createdSubCommanders[entity] = commander

        return commander
    }

    fun createRepoCommanderForBlock(block: PipelineBlock<*, *>): LocalRepositoryCommander {

        val commander = LocalRepositoryCommander(createSubDir(block.name))
        createdSubCommanders[block] = commander

        return commander
    }


    fun getAbsolutePath(path: String): String {
        return workingDir.resolve(path).toString()
    }
}
