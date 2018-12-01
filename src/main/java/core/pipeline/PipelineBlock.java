package core.pipeline;

import core.data.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Block is used as a core part of pipeline.
 *
 * <p> Pipeline consists of different blocks that are sending data to each other.
 *
 * @param <I>  input that is expected in a block to be received from other PipelineBlocks in the pipeline
 * @param <O> output that is produced by PipelineBlock and propagated to listening blocks in pipeline.
 */
public abstract class PipelineBlock<I extends Data, O extends Data> {


    protected List<PipelineBlock<O, ?>> listeners;

    public PipelineBlock() {
        this.listeners = new ArrayList<>();
    }

    public abstract void inputDataReady(PipelineBlock<?, I> sender, I input);

    public void addListener(PipelineBlock<O, ?> listener) {
        this.listeners.add(listener);
    }

    public void notifyListeners(PipelineBlock<?, O> sender, O data) {
        listeners.forEach(pipelineBlock -> pipelineBlock.inputDataReady(sender, data));
    }

    //TODO: (avlomakin) smt like protected cacheData(TInput data) method that will manage cache based on memory policy
}
