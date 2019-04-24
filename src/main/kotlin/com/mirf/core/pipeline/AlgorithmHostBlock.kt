package com.mirf.core.pipeline


import com.mirf.core.algorithm.Algorithm
import com.mirf.core.algorithm.asAlg
import com.mirf.core.data.Data

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
open class AlgorithmHostBlock<I : Data, O : Data>(
        private var algorithm: Algorithm<I, O>,
        name: String = algorithm.toString(),
        pipelineKeeper: PipelineKeeper = DummyPipeKeeper()) : PipelineBlock<I, O>(name, pipelineKeeper) {

    override fun flush() {
        cachedInput = null
        cachedOutput = null
    }

    constructor(algorithm: (I) -> O, name: String = "", pipelineKeeper: PipelineKeeper = DummyPipeKeeper()) : this(algorithm.asAlg(), name, pipelineKeeper)

    var enabled: Boolean = true
    private var cachedInput: I? = null
    private var cachedOutput: O? = null

    override fun inputReady(sender: Any, input: I) {
        if (enabled) {

            val record = pipelineKeeper.session.addNew("[$name]: algorithm execution")

            try {
                cachedInput = input
                cachedOutput = algorithm.execute(input)
                onDataReady(this, cachedOutput as O)
                record.setSuccess()
            } catch (e: PipelineException) {
                record.setError()
                throw e
            }

        }
    }
}
