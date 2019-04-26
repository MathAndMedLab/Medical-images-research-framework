package com.mirf.core.repository

/**
 * Provides method for repository interaction. To access your data storage,
 * use one of the implementations or create custom implementation
 */
interface RepositoryCommander {

    //TODO: (avlomakin) add javadoc
    @Throws(RepositoryCommanderException::class)
    fun getFile(link: String): ByteArray

    @Throws(RepositoryCommanderException::class)
    fun getSeriesFileLinks(link: String): Array<String>

    /**
     * Saves file to repository
     * @param file raw file bytes
     * @param link target location
     * @param filename Name + extension of the file
     */
    @Throws(RepositoryCommanderException::class)
    fun saveFile(file: ByteArray, link: String, filename: String): String

    fun createRepoCommanderFor(entity: Any): RepositoryCommander

    fun generateLink(type: LinkType): String
}

