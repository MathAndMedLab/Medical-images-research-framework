package features.repository

import core.repository.LinkType
import core.repository.RepositoryCommander
import core.repository.RepositoryCommanderException

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.collections.HashSet

/**
 * Local file system commander, Link = path on a filesystem
 */
class LocalRepositoryCommander(val workingDir: String = "") : RepositoryCommander {

    private val tempFiles = HashSet<String>()

    override fun generateLink(type: LinkType): String {
        return UUID.randomUUID().toString()
    }

    @Throws(RepositoryCommanderException::class)
    override fun getFile(link: String): ByteArray {
        val filePath = Paths.get(workingDir, link)
        try {
            return Files.readAllBytes(filePath)
        } catch (e: IOException) {
            throw RepositoryCommanderException("Failed to read file bytes", e)
        }

    }

    override fun getSeriesFileLinks(link: String): Array<String> {
        val pathUri = Paths.get(workingDir, link).toUri()
        return File(pathUri).listFiles().filter { it.isFile }.map { it.path }.toTypedArray()
    }

    @Throws(RepositoryCommanderException::class)
    override fun saveFile(file: ByteArray, link: String, filename: String): String {
        try {
            val streamLink = Paths.get(workingDir, link, filename).toString()
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
    fun createTempDir(): String {
        val tempDir = UUID.randomUUID().toString()
        val path = Paths.get(workingDir, tempDir)

        Files.createDirectory(path)

        tempFiles.add(tempDir)
        return tempDir
    }

    /**
     * Removes all temp directories and files from the file system.
     * If any of the file is in use, it will be skipped
     */
    fun cleanupSafe() {
        for (fileString in tempFiles) {
            try {
                Files.delete(Paths.get(fileString))
            } catch (e: Exception) {
            }
        }
    }

    fun getAbsolutePath(path: String): String {
        return Paths.get(workingDir, path).toString()
    }
}
