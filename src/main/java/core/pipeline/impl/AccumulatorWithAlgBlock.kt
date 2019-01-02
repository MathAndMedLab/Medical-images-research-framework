package core.pipeline.impl

import core.algorithm.Algorithm
import core.data.Data
import core.pipeline.PipelineBlock
import core.data.CollectionData

import kotlin.collections.ArrayList

/**
 * [PipelineBlock] that accumulates inputs of the same type
 * @param <I> input type
 * @param <O> output type
</O></I> */
class AccumulatorWithAlgBlock<I : Data, O : Data>(private var algorithm: Algorithm<CollectionData<I>, O>, private val connections: Int) : PipelineBlock<I, O>() {

    var enabled = true
    private var inputs = ArrayList<I>()

    override fun inputDataReady(sender: PipelineBlock<*, I>?, input: I) {
        if (enabled) {
            inputs.add(input)
            if (inputs.size == connections) {
                val collectionData = CollectionData(inputs)

                val result = algorithm.execute(collectionData)
                notifyListeners(this, result)
            }

            if(inputs.size > connections)
                throw Exception("received too many signals. Expected value - $connections")
        }
    }

    override fun flush() {
        inputs = ArrayList()
    }
}
