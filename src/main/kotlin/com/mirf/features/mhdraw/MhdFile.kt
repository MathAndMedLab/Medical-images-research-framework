package com.mirf.features.mhdraw

import ij.ImagePlus
import ij.io.FileInfo
import ij.io.FileOpener
import java.io.File


class MhdFile(private val fileInfo: FileInfo) {

    val image: ImagePlus
        get() {
            val fo = FileOpener(fileInfo)
            return fo.open(false)
        }

    companion object {

        fun load(MhdFilename: String): MhdFile {
            var offsets: DoubleArray
            var spacings: DoubleArray

            val fi = FileInfo()
            fi.fileFormat = FileInfo.RAW
            fi.directory = File(MhdFilename).parent

            val file = File(MhdFilename)

            file.forEachLine {
                val parameter = MhdParameter.parseLine(it)
                when (parameter.name) {
                    "BinaryDataByteOrderMSB" -> {
                        val value = parameter.getBoolean()
                        fi.intelByteOrder = !value
                    }
                    "Offset" -> {
                        offsets = DoubleArray(parameter.size)
                        for (i in 0 until parameter.size) {
                            offsets[i] = parameter.getDouble(i)
                        }
                    }
                    "ElementSpacing" -> {
                        spacings = DoubleArray(parameter.size)
                        for (i in 0 until parameter.size) {
                            spacings[i] = parameter.getDouble(i)
                        }
                    }
                    "DimSize" -> {
                        fi.width = parameter.getInt(0)
                        fi.height = parameter.getInt(1)
                        fi.nImages = parameter.getInt(2)
                    }
                    "ElementType" -> {
                        fi.fileType = getFileType(parameter.getString())
                    }
                    "ElementDataFile" -> {
                        fi.fileName = parameter.getString()
                    }
                }
            }
            return MhdFile(fi)
        }

        private fun getFileType(value: String): Int {
            return when (value) {
                "MET_USHORT" -> FileInfo.GRAY16_UNSIGNED
                "MET_SHORT" -> FileInfo.GRAY16_SIGNED
                else -> FileInfo.UNKNOWN
            }
        }
    }

    class MhdParameter(val name: String, parameters: String) {

        private val parameters: Array<String> = parameters.splitLine(" ")
        val size: Int = this.parameters.size

        fun getInt(index: Int = 0): Int = parameters[index].toInt()
        fun getDouble(index: Int = 0): Double = parameters[index].toDouble()
        fun getBoolean(index: Int = 0): Boolean = parameters[index].toBoolean()
        fun getString(index: Int = 0): String = parameters[index]

        companion object {
            fun parseLine(line: String): MhdParameter {
                val lines = line.splitLine(" = ")
                return MhdParameter(lines[0], lines[1])
            }
        }
    }
}

private fun String.splitLine(separator: String): Array<String> {
    return this.split(separator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
}