package core.pipeline.impl;

import core.algorithm.Algorithm;
import core.data.Data;
import core.pipeline.PipelineBlock;
import core.data.CollectionData;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link PipelineBlock} that accumulates inputs of the same type
 * @param <I> input type
 * @param <O> output type
 */
public class AccumulatorWithAlgBlock<I extends Data, O extends Data> extends PipelineBlock<I, O> {

    protected Algorithm<CollectionData<I>, O> algorithm;

    public boolean enabled = true;

    private int connections;
    private List<I> inputs;

    public AccumulatorWithAlgBlock(Algorithm<CollectionData<I>, O> algorithm, int connections) {
        super();
        this.algorithm = algorithm;
        this.connections = connections;
        inputs = new ArrayList<>();
    }

    @Override
    public void inputDataReady(PipelineBlock<?, I> sender, I input) {
        if (enabled) {
            inputs.add(input);
            if(inputs.size() == connections){
                var collectionData = new CollectionData<>(inputs);

                //TODO:(avlomakin) if !cacheEnabled {inputs = new ()} to prevent memory leaking
                inputs = new ArrayList<>();

                var result =  algorithm.execute(collectionData);
                notifyListeners(this, result);
            }
        }
    }
}
