package com.mirf.features.machinelearning

import org.tensorflow.Graph
import org.tensorflow.Session
import org.tensorflow.Tensor
import org.tensorflow.TensorFlow
import com.mirf.features.machinelearning.tensorflow.TensorFlowInferenceInterface

/**
 * PbImporter is used to load and run .pb files of Tensorflow models
 */
class TensorflowInference//
(modelFile: String, inputName: String, outputName: String) {
    //    private static final String TAG = PbImporter.class.getSimpleName();
    //
    private var modelFile: String? = null
    private var inputName: String? = null
    private var outputName: String? = null
    private var inferenceInterface: TensorFlowInferenceInterface? = null
    private var numOfOutputDims: Int = 0
    private var numOfOutputValues: Int = 0

    init {
        try {
            val inferenceInterface = TensorFlowInferenceInterface(null, modelFile)
            this.inferenceInterface = inferenceInterface
            this.modelFile = modelFile
            this.inputName = inputName
            this.outputName = outputName
            this.numOfOutputDims = inferenceInterface.graph().operation(outputName).output<Any>(0).shape().numDimensions()
            var numOfOutputs = 1
            for (i in 0 until numOfOutputDims) {
                numOfOutputs *= inferenceInterface.graph().operation(outputName).output<Any>(0).shape().size(i).toInt()
                //                Log.e(TAG, "Output for cur size: " + String.valueOf((int) inferenceInterface.graph().operation(outputName).output(0).shape().size(i)));
            }
            this.numOfOutputValues = numOfOutputs
        } catch (e: UnsatisfiedLinkError) {
            //            Log.e(TAG, "failed to load Tensorflow model: " + e.getMessage());
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

    // Check that Tensorflow is working
    companion object {
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            Graph().use { g ->
                val value = "Hello from " + TensorFlow.version()

                // Construct the computation graph with a single operation, a constant
                // named "MyConst" with a value "value".
                Tensor.create(value.toByteArray(charset("UTF-8"))).use { t ->
                    // The Java API doesn't yet include convenience functions for adding operations.
                    g.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build()
                }

                // Execute the "MyConst" operation in a Session.
                Session(g).use { s -> s.runner().fetch("MyConst").run()[0].use { output -> println(String(output.bytesValue())) } }// Generally, there may be multiple output tensors,
                // all of them must be closed to prevent resource leaks.
            }
        }
    }
}