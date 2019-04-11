package features.deeplearning.tensorflow

import org.junit.Test

class TensorflowModelTest {

    @Test
    fun readDicomImageAttributesFromLocalFile_localDicom_readsWithoutErrors() {
        val modelName = "src/test/resources/exampleDicom.dcm"
        val va = TensorflowModelInterface(modelName,"","")
    }

}