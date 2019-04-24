package com.mirf.features.deeplearning.tensorflow

import java.io.InputStream

/**
 * TensorflowModelInterface is used to load and run .pb files of Tensorflow models
 *
 * @param modelFile is the String location of the model
 * @param inputName is the name of the input node in tensorflow model
 * @param outputName is the name of the output node in tensorflow model
 * @param outputDims are the output nodes dimensions
 */
class TensorflowModelInterface (input: InputStream?, modelFile: String, inputName: String, outputName: String, vararg outputDims: Int){
    private var modelFile: String? = null
    private var numOfOutputDims: Int = 0
    private var numOfOutputValues: Int = 0

    public var inferenceInterface: TensorflowInferenceInterface? = null
    public var inputName: String? = null
    public var outputName: String? = null

    init {
        try {
            val inferenceInterface = TensorflowInferenceInterface(input, modelFile)
            this.inferenceInterface = inferenceInterface
            this.modelFile = modelFile
            this.inputName = inputName
            this.outputName = outputName
            this.numOfOutputDims = inferenceInterface.graph().operation(outputName).output<Any>(0).shape().numDimensions()
            var numOfOutputs = 1
            for (el in outputDims) {
                numOfOutputs *= el.toInt()
            }
            this.numOfOutputValues = numOfOutputs
        } catch (e: Throwable) {
        }

    }

    fun runModel(src: FloatArray, vararg dims: Long): FloatArray {
        try {
            inferenceInterface!!.feed(inputName!!, src, *dims)
        } catch (e: Error) {
        }

        val outputNames = this!!.outputName?.let { arrayOf<String>(it) }
        val outputValues = FloatArray(numOfOutputValues)
        try {
            if (outputNames != null) {
                inferenceInterface!!.run(outputNames)
            }
        } catch (e: Error) {
        }

        try {
            // Copy the output Tensor back into the output array.
            inferenceInterface!!.fetch(outputName!!, outputValues)
        } catch (e: Error) {

        }
        return outputValues
    }
}