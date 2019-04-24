package com.mirf.core.data

/**
 * [Data] that represents single file
 */
class FileData(val fileBytes: ByteArray, val name: String, val extension: String) : MirfData()
