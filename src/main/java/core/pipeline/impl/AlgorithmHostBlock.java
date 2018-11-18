package core.pipeline.impl;


import core.algorithm.Algorithm;
import core.algorithm.ReportableAlgorithm;
import core.data.report.Report;
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

    Algorithm<TInput, TOutput> algorithm;

    public boolean enabled = true;
    private TInput cachedInput;
    private TOutput cachedOutput;

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

    public Report getReport()
    {
        if(algorithm instanceof ReportableAlgorithm)
            return ((ReportableAlgorithm<TInput, TOutput>) algorithm).getReport(cachedInput,cachedOutput);
        else
            throw new RuntimeException(String.format("Algorithm %s cannot be reported", algorithm.getClass().getName()));
    }
}
