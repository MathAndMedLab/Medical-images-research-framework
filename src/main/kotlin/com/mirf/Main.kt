package com.mirf

import com.mirf.playground.DicomImageCircleMaskApplier
import com.mirf.playground.NiftiTest

object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        runDicom()
    }

    /**
     * Pipeline for DICOM reader module test
     */
    fun runDicom() {
        val dicomFolder = javaClass.getResource("/dicoms").path.fixForCurrentPlatform()
        val resultFolder = javaClass.getResource("/reports").path.fixForCurrentPlatform()
        DicomImageCircleMaskApplier().exec(dicomFolder, resultFolder)
    }

    /**
     * Pipeline for NIFTI reader module test
     */
    fun runNifti() {
        val niftiFile = javaClass.getResource("/nifti/brain.nii").path
        val mhd = javaClass.getResource("/raw/brain.mhd").path
        val resultFolder = javaClass.getResource("/reports").path
        NiftiTest().exec(niftiFile, mhd, resultFolder)
    }
}

private fun String.fixForCurrentPlatform(): String {
    return if (OSValidator.isWindows)
        this.removePrefix("/")
    else
        this
}

object OSValidator {

    private val OS = System.getProperty("os.name").toLowerCase()

    val isWindows: Boolean
        get() = OS.indexOf("win") >= 0

    val isMac: Boolean
        get() = OS.indexOf("mac") >= 0

    val isUnix: Boolean
        get() = OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0

    val isSolaris: Boolean
        get() = OS.indexOf("sunos") >= 0
}
