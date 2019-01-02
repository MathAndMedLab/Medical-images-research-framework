package core.pipeline

import core.data.Data

import kotlin.collections.ArrayList

/**
 * Block is used as a core part of pipeline.
 *
 *
 *  Pipeline consists of different blocks that are sending data to each other.
 *
 * @param <I>  input that is expected in a block to be received from other PipelineBlocks in the pipeline
 * @param <O> output that is produced by PipelineBlock and propagated to listening blocks in pipeline.
</O></I> */
abstract class PipelineBlock<I : Data, O : Data> {

    protected var listeners: ArrayList<PipelineBlock<O, *>> = ArrayList()

    abstract fun inputDataReady(sender: PipelineBlock<*, I>?, input: I)

    open fun addListener(listener: PipelineBlock<O, *>) {
        this.listeners.add(listener)
    }

    fun notifyListeners(sender: PipelineBlock<*, O>, data: O) {
        listeners.forEach { pipelineBlock -> pipelineBlock.inputDataReady(sender, data) }
    }

    abstract fun flush()

    //TODO: (avlomakin) smt like protected cacheData(TInput data) method that will manage cache based on memory policy
}
