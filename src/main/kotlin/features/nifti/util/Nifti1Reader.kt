package features.nifti.util

import features.nifti.NiftiImageSeries
import ij.ImagePlus
import ij.plugin.Nifti_Reader
import java.io.File


object Nifti1Reader {

    fun read(link: String): NiftiImageSeries {
        val directory: String
        val name: String

        val i = link.lastIndexOf(File.separator)

        if (i != -1) {
            directory = link.substring(0, i)
            name = link.substring(i + 1)
        } else {
            name = link
            directory = ""
        }

        val niftiReader = Nifti_Reader()
        niftiReader.run(link)

        return NiftiImageSeries(niftiReader.imageStack)
    }

}