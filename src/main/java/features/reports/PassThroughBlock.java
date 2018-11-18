package features.reports;

import core.algorithm.Algorithm;
import core.data.Data;
import core.pipeline.PipelineBlock;

public class PassThroughBlock<Input extends Data, Output extends Data> extends PipelineBlock<Input, Output> {

    private Algorithm<PipelineBlock<?, Input>, Output> algorithm;

    public boolean enabled = true;

    public PassThroughBlock(Algorithm<PipelineBlock<?, Input>, Output> algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public void InputDataReady(PipelineBlock<?, Input> sender, Input input) {
        if(enabled) {
            Output result = algorithm.execute(sender);
            notifyListeners(this, result);
        }
    }
}
