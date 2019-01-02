package core.pipeline.impl


import core.algorithm.Algorithm
import core.data.Data
import core.pipeline.PipelineBlock

/**
 * A [PipelineBlock], that is storing and executing [Algorithm] and
 * transferring its results to other blocks.
 *
 *
 * Block does not necessary has to have algorithm.
 *
 * @param <I> is an input type of the [Algorithm], hosted by this Block.
 * @param <O> is an output type of the [Algorithm], hosted by this Block.
</O></I> */
class AlgorithmHostBlock<I : Data, O : Data>(private var algorithm: Algorithm<I, O>) : PipelineBlock<I, O>() {

    override fun flush() {
        cachedInput = null
        cachedOutput = null
    }

    var enabled = true
    private var cachedInput: I? = null
    private var cachedOutput: O? = null

    override fun inputDataReady(sender: PipelineBlock<*, I>?, input: I) {
        if (enabled) {
            cachedInput = input
            cachedOutput = algorithm.execute(input)
            notifyListeners(this, cachedOutput as O)
        }
    }
}
