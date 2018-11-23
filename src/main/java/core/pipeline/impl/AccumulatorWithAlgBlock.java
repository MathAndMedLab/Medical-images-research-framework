package core.pipeline.impl;

import core.algorithm.Algorithm;
import core.data.Data;
import core.pipeline.PipelineBlock;
import features.reports.pdf.CollectionData;

import java.util.ArrayList;
import java.util.List;

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
                CollectionData<I> collectionData = new CollectionData<>(inputs);

                //TODO:(avlomakin) if !cacheEnabled {inputs = new ()} to prevent memory leaking
                inputs = new ArrayList<>();

                O result =  algorithm.execute(collectionData);
                notifyListeners(this, result);
            }
        }
    }
}
