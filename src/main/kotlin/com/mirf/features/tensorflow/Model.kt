package com.mirf.features.tensorflow

import org.tensorflow.Tensor

/*
*Concrete tensorflow model stub, handles session and resources
 */
open class Model(val graph: ByteArray) {

    fun run(tensor: Tensor<in Any>){

    }
}