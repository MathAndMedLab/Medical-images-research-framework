package com.mirf.features.tensorflow

import org.tensorflow.Tensor

open class ClassificationResult {

}

class BinaryClassificationResult(val results: BooleanArray) : ClassificationResult() {

    companion object {
        fun createFromTensor(vararg tensors:  Tensor<Number>):List<BinaryClassificationResult> {

            val result :ArrayList<BinaryClassificationResult> = arrayListOf()

            for (tensor in tensors){
                val buffer : Array<Number> = createBuffer(tensor)
                tensor.copyTo(buffer)
                result.add(BinaryClassificationResult(buffer.map { x -> x.toByte() > 0 }.toBooleanArray()))
            }

            return result
        }

        private fun createBuffer(tensor: Tensor<Number>): Array<Number> {
            val shape = tensor.shape()
            return Array(shape[0].toInt()) {0}
        }
    }
}
