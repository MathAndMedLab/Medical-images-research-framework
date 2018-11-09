package core.pipeline;


import core.algorithm.Algorithm;
import model.data.Data;
import model.pipeline.PipelineBlock;

/**
 * FYI
 * block not necessary has to have algorithm
 */
public class AlgorithmHostBlock<TInput extends Data, TOutput extends Data> extends PipelineBlock<TInput, TOutput> {

    Algorithm<TInput, TOutput> algorithm;

    public boolean enabled = true;

    public AlgorithmHostBlock(Algorithm<TInput, TOutput> algorithm) {
        super();
        this.algorithm = algorithm;
    }

    @Override
    public void InputDataReady(PipelineBlock<?, TInput> sender, TInput tInput) {
        if(enabled) {
            TOutput result = algorithm.execute(tInput);
            notifyListeners(this, result);
        }
    }
}
