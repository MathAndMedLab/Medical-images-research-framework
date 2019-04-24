package com.mirf.features.deeplearning.tensorflow

import org.junit.Assert
import org.junit.Test
import kotlin.math.roundToInt

class TensorflowModelTest {

    @Test
    fun TensorflowModelInterface_loadModel_readsModelWithoutErrors() {
        val modelName = "src/test/resources/xor_model.pb"
        val tfModel = TensorflowModelInterface(null, modelName, "my_input/X", "my_output/Sigmoid", 1)
        // check that input and output are set correctly
        Assert.assertEquals("my_input/X", tfModel.inputName)
        Assert.assertEquals("my_output/Sigmoid", tfModel.outputName)
    }

    @Test
    fun TensorflowModelInterface_runXORmodel_outputsResult() {
        val modelName = "src/test/resources/xor_model.pb"
        val tfModel = TensorflowModelInterface(null, modelName, "my_input/X", "my_output/Sigmoid", 1)

        // check that input and output are set correctly
        var inputArr: FloatArray = floatArrayOf(1.0F, 1.0F)
        var res = tfModel.runModel(inputArr, 1, 2)[0].roundToInt()
        Assert.assertEquals(0, res)

        inputArr = floatArrayOf(1.0F, 0.0F)
        res = tfModel.runModel(inputArr, 1, 2)[0].roundToInt()
        Assert.assertEquals(1, res)

        inputArr = floatArrayOf(0.0F, 0.0F)
        res = tfModel.runModel(inputArr, 1, 2)[0].roundToInt()
        Assert.assertEquals(0, res)

        inputArr = floatArrayOf(0.0F, 1.0F)
        res = tfModel.runModel(inputArr, 1, 2)[0].roundToInt()
        Assert.assertEquals(1, res)
    }

    @Test
    fun TensorflowModelInterface_runClassificationOnImagemodel_outputsResult() {
        val img_size = 128
        val modelName = "src/test/resources/tf_model.pb"
        val tfModel = TensorflowModelInterface(null, modelName, "conv2d_1_input_1", "activation_5_1/Sigmoid", 1, 1)

        val bigArr = FloatArray(img_size * img_size * 3)
        // check that input and output are set correctly
        var res = tfModel.runModel(bigArr, 1, 128, 128, 3)[0].roundToInt()
        Assert.assertEquals(1, res)
    }
}