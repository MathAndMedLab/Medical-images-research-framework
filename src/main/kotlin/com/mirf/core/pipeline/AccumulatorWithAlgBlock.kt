package com.mirf.core.pipeline

import com.mirf.core.algorithm.Algorithm
import com.mirf.core.data.Data
import com.mirf.core.data.CollectionData

/**
 * [PipelineBlock] that accumulates inputs of the same type
 * @param <I> input type
 * @param <O> output type
</O></I> */
class AccumulatorWithAlgBlock<I : Data, O : Data>(
        private var algorithm: Algorithm<CollectionData<I>, O>,
        private val connections: Int,
        name: String = "accumulator for $algorithm",
        pipelineKeeper: PipelineKeeper = DummyPipeKeeper()) : PipelineBlock<I, O>(name, pipelineKeeper) {

    var enabled = true
    private var inputs = ArrayList<I>()

    override fun inputReady(sender: Any, input: I) {
        if (enabled) {
            inputs.add(input)

            if (inputs.size == connections) {

                pipelineKeeper.session.addSuccess("[$name] : All connections are ready")
                val record = pipelineKeeper.session.addNew("[$name]: algorithm execution")


                val collectionData = CollectionData(inputs)

                try {
                    val result = algorithm.execute(collectionData)
                    onDataReady(this, result)
                    record.setSuccess()
                } catch (e: PipelineException) {
                    record.setError()
                    throw e
                }
            }

            if (inputs.size > connections)
                throw Exception("received too many signals. Expected value - $connections")
        }
    }

    override fun flush() {
        inputs = ArrayList()
    }
}
