import modules.ms.MsPipeline
import playground.DicomImageCircleMaskApplier
import playground.NiftiTest

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        runMs()
    }

    /**
     * Pipeline for MS report generation
     */
    fun runMs() {
        val baseline = javaClass.getResource("/nifti/test_patient/baseline/T1.nii").path.removePrefix("/")
        val followup = javaClass.getResource("/nifti/test_patient/followup/T1.nii").path.removePrefix("/")
        val resultFolder = javaClass.getResource("/reports").path.removePrefix("/")
        MsPipeline().exec(baseline, followup, resultFolder)
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
