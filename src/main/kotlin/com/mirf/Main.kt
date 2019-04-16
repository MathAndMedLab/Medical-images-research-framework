package com.mirf

import com.mirf.core.common.ExternalResourceLoader
import com.mirf.core.log.MirfLogFactory
import com.mirf.playground.DicomImageCircleMaskApplier
import com.mirf.playground.NiftiTest

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        //val props = Properties()
        //props.load(javaClass.getResourceAsStream("/log4j2.properties"))
        //PropertyConfigurator.configure(props)

        ExternalResourceLoader().loadExternalResources()

        try {
        } catch (e: Exception) {
            MirfLogFactory.currentLogger.error("ERROR: $e")
        }
    }

    /**
     * Pipeline for DICOM reader module test
     */
    fun runDicom() {
        val dicomFolder = javaClass.getResource("/dicoms").path
        val resultFolder = javaClass.getResource("/reports").path
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
