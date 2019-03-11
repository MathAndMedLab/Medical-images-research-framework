import playground.DicomImageCircleMaskApplier
import playground.NiftiTest

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        runNifti()
    }

    fun runDicom(){
        val dicomFolder = javaClass.getResource("/dicoms").path
        val resultFolder = javaClass.getResource("/reports").path
        DicomImageCircleMaskApplier().exec(dicomFolder, resultFolder)
    }

    fun runNifti(){
        val niftiFile = javaClass.getResource("/nifti/brain.nii").path
        val resultFolder = javaClass.getResource("/reports").path
        NiftiTest().exec(niftiFile, resultFolder)
    }
}
