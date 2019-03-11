package features.repository

import core.repository.RepositoryCommander
import core.repository.RepositoryCommanderException

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Local file system commander, Link = path on a filesystem
 */
class LocalRepositoryCommander : RepositoryCommander {

    @Throws(RepositoryCommanderException::class)
    override fun getFile(link: String): ByteArray {
        val filePath = Paths.get(link)
        try {
            return Files.readAllBytes(filePath)
        } catch (e: IOException) {
            throw RepositoryCommanderException("Failed to read file bytes", e)
        }

    }
    override fun getSeriesFileLinks(link: String): Array<String> {
        return File(link).listFiles().filter { it.isFile }.map { it.path }.toTypedArray()
    }

    @Throws(RepositoryCommanderException::class)
    override fun saveFile(file: ByteArray, link: String, filename: String) {
        try {
            val stream = FileOutputStream("$link/$filename")
            stream.write(file)
        } catch (e: IOException) {
            throw RepositoryCommanderException("Failed to write file bytes", e)
        }
    }
}
