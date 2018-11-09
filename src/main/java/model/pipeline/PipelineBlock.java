package model.pipeline;

import model.data.Data;

import java.util.ArrayList;
import java.util.List;

public  abstract class PipelineBlock<TInput extends Data, TOutput extends Data> {


    protected List<PipelineBlock<TOutput, ?>> listeners;

    public PipelineBlock() {
        this.listeners = new ArrayList<>();
    }

    public abstract void InputDataReady(PipelineBlock<?, TInput> sender, TInput input);

    public void addListener(PipelineBlock<TOutput, ?> listener) {
        this.listeners.add(listener);
    }

    public void notifyListeners(PipelineBlock<?, TOutput> sender, TOutput data){
        listeners.forEach(pipelineBlock -> pipelineBlock.InputDataReady(sender, data));
    }

    //TODO: (avlomakin) smt like protected cacheData(TInput data) method that will manage cache based on memory policy
}
