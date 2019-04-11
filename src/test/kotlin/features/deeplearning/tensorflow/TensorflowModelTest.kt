package features.deeplearning.tensorflow

import org.junit.Test

class TensorflowModelTest {

    @Test
    fun TensorflowModelInterface_loadModel_readsModelWithoutErrors() {
        val modelName = "src/test/resources/xor_model.pb"
        val va = TensorflowModelInterface(modelName,"my_input/X","my_output/Sigmoid")
    }

}