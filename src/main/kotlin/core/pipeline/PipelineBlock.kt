package core.pipeline

import core.data.Data
import core.common.EventManager

/**
 * Block is used as a core part of pipeline.
 *
 *
 *  Pipeline consists of different blocks that are sending data to each other.
 *
 * @param <I>  input that is expected in a block to be received from other PipelineBlocks in the pipeline
 * @param <O> output that is produced by PipelineBlock and propagated to listening blocks in pipeline.
</O></I> */
abstract class PipelineBlock<I : Data, O : Data>(
        val name: String,
        val pipelineKeeper: PipelineKeeper) {

    protected open val onDataReady : EventManager<O> = EventManager()

    open val dataReady
        get() = onDataReady.event

    abstract fun inputReady(sender: Any, input: I)

    /**
     * Flushes all cached data of the [PipelineBlock]
     */
    abstract fun flush()

    //TODO: (avlomakin) smt like protected cacheData(TInput data) method that will manage cache based on memory policy
}
