package com.mirf.features.nifti.util

import ij.ImagePlus
import ij.plugin.Nifti_Reader


object Nifti1Reader {

    fun read(link: String): ImagePlus {
        val niftiReader = Nifti_Reader()
        niftiReader.run(link)

        return niftiReader
    }

}