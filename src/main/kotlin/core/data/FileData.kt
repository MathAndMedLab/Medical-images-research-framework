package core.data

import core.data.Data

/**
 * [Data] that represents single file
 */
class FileData(val fileBytes: ByteArray, val name: String, val extension: String) : MirfData()
