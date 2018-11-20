package core.pipeline.impl;


import core.algorithm.Algorithm;
import core.data.Data;
import core.pipeline.PipelineBlock;

/**
 * A {@link PipelineBlock}, that is storing and executing {@link Algorithm} and
 * transferring its results to other blocks.
 *
 * <p>Block does not necessary has to have algorithm.
 *
 * @param <I> is an input type of the {@link Algorithm}, hosted by this Block.
 * @param <O> is an output type of the {@link Algorithm}, hosted by this Block.
 */
public class AlgorithmHostBlock<I extends Data, O extends Data> extends PipelineBlock<I, O> {

    private Algorithm<I, O> algorithm;

    public boolean enabled = true;

    public AlgorithmHostBlock(Algorithm<I, O> algorithm) {
        super();
        this.algorithm = algorithm;
    }

    @Override
    public void inputDataReady(PipelineBlock<?, I> sender, I tInput) {
        if (enabled) {
            O result = algorithm.execute(tInput);
            notifyListeners(this, result);
        }
    }
}
