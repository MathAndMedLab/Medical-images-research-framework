package core.pipeline.impl;


import core.algorithm.Algorithm;
import core.pipeline.PipelineBlock;
import core.data.Data;

/**
 * A {@link PipelineBlock}, that is storing and executing {@link Algorithm} and
 * transferring its results to other blocks.
 *
 * <p>Block does not necessary has to have algorithm.
 *
 * @param <TInput>  is an input type of the {@link Algorithm}, hosted by this Block.
 * @param <TOutput> is an output type of the {@link Algorithm}, hosted by this Block.
 */
public class AlgorithmHostBlock<TInput extends Data, TOutput extends Data> extends PipelineBlock<TInput, TOutput> {

    protected Algorithm<TInput, TOutput> algorithm;

    public boolean enabled = true;
    protected TInput cachedInput;
    protected TOutput cachedOutput;

    public AlgorithmHostBlock(Algorithm<TInput, TOutput> algorithm) {
        super();
        this.algorithm = algorithm;
    }

    @Override
    public void InputDataReady(PipelineBlock<?, TInput> sender, TInput tInput) {
        if (enabled) {
            cachedInput = tInput;
            cachedOutput = algorithm.execute(tInput);
            notifyListeners(this, cachedOutput);
        }
    }
}
