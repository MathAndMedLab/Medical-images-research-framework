package core.pipeline.impl;

import core.algorithm.Algorithm;
import core.pipeline.PipelineBlock;
import core.data.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple pipeline that is used to sequentially apply Algorithms to the data flow
 */
public class SequentialPipeline {

    public SequentialPipeline() {
        this.blocks = new ArrayList<>();
    }

    private List<PipelineBlock> blocks;

    public void run(Data initialData) {
        blocks.get(0).inputDataReady(null, initialData);
    }

    /**
     * Creates {@link AlgorithmHostBlock} for the specified Algorithm and calls {@link #add(PipelineBlock) add} method
     *
     * @param algorithm algorithm to be appended to this pipeline
     * @param <TIn> is an input type of the {@link Algorithm}
     * @param <TOut> is an output type of the {@link Algorithm}
     */
    public <TIn extends Data, TOut extends Data> void add(Algorithm<TIn, TOut> algorithm)
    {
        AlgorithmHostBlock<TIn, TOut> block = new AlgorithmHostBlock<>(algorithm);
        add(block);
    }

    /**
     * Appends specified block the end of this pipeline, connects it to the previous block if exists
     *
     * @param block block to be appended to this pipeline
     * @param <TIn> is an input type of the {@link PipelineBlock}
     * @param <TOut> is an output type of the {@link PipelineBlock}
     */
    public <TIn extends Data, TOut extends Data> void add(PipelineBlock<TIn, TOut> block)
    {
        if(!blocks.isEmpty())
            blocks.get(blocks.size() - 1).addListener(block);

        blocks.add(block);
    }
}

