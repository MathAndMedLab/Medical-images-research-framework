package core.pipeline.impl

import core.data.Data
import core.pipeline.PipelineBlock

import java.util.function.Consumer


/**
 * A [PipelineBlock], that is only executing [<] provided.
 * Subscription to this block is not allowed and will lead to an error.
 */
class ConsumerBlock(private val consumer: Consumer<Data>) : PipelineBlock<Data, Data>() {

    override fun flush() {}

    override fun inputDataReady(sender: PipelineBlock<*, Data>?, input: Data) {
        consumer.accept(input)
    }

    override fun addListener(listener: PipelineBlock<Data, *>) {
        throw RuntimeException("Subscription to the ConsumerBlock block is not allowed")
    }
}
