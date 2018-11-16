package core.pipeline.impl;

import core.data.Data;
import core.pipeline.PipelineBlock;

import java.util.function.Consumer;


/**
 *  A {@link PipelineBlock}, that is only executing {@link Consumer<Data>} provided.
 *  Subscription to this block is not allowed and will lead to an error.
 */
public class ConsumerBlock extends PipelineBlock<Data, Data> {

    private Consumer<Data> consumer;

    public ConsumerBlock(Consumer<Data> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void InputDataReady(PipelineBlock<?, Data> sender, Data data) {
        consumer.accept(data);
    }

    @Override
    public void addListener(PipelineBlock<Data, ?> listener) {
        throw new RuntimeException("Subscription to the ConsumerBlock block is not allowed");
    }
}
