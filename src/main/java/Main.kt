import playground.DicomImageCircleMaskApplier

object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        val dicomFolder = javaClass.getResource("/dicoms").path
        val resultFolder = javaClass.getResource("/reports").path
        DicomImageCircleMaskApplier().exec(dicomFolder, resultFolder)
    }
}
