package core.pipeline.impl;

import core.algorithm.Algorithm;
import core.data.Data;
import core.pipeline.PipelineBlock;
import features.reports.pdf.CollectionData;

import java.util.ArrayList;
import java.util.List;

public class AccumulatorWithAlgBlock<Input extends Data, Output extends Data> extends PipelineBlock<Input, Output> {

    protected Algorithm<CollectionData<Input>, Output> algorithm;

    public boolean enabled = true;

    private int connections;
    private List<Input> inputs;

    public AccumulatorWithAlgBlock(Algorithm<CollectionData<Input>, Output> algorithm, int connections) {
        super();
        this.algorithm = algorithm;
        this.connections = connections;
        inputs = new ArrayList<>();
    }

    public void InputDataReady(PipelineBlock<?, Input> sender, Input input) {
        if (enabled) {
            inputs.add(input);
            if(inputs.size() == connections){
                CollectionData<Input> collectionData = new CollectionData<>(inputs);

                //TODO:(avlomakin) if !cacheEnabled {inputs = new ()} to prevent memory leaking
                inputs = new ArrayList<>();

                Output result =  algorithm.execute(collectionData);
                notifyListeners(this, result);
            }
        }
    }
}
