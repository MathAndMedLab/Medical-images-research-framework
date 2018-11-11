package core.pipeline.impl;

import core.pipeline.PipelineBlock;
import core.data.Data;

import java.util.List;

// TODO(avlomakin): add javadoc here
public class SequentialPipeline {

    public SequentialPipeline(List<PipelineBlock> blocks) {
        this.blocks = blocks;
    }

    List<PipelineBlock> blocks;

    public void Run(Data initialData)
    {
        blocks.get(0).InputDataReady(null, initialData);
    }

    public static SequentialPipeline CreateFromBlockList(List<PipelineBlock> blocks, boolean connected) throws NoSuchMethodException {
        //this lang is so bad
        blocks.add(new ResultTerminalPrinter());

        if(!connected){
            for(int i = 0; i < blocks.size() - 1; i++)
                blocks.get(i).addListener(blocks.get(i + 1));
        }

        return new SequentialPipeline(blocks);
    }
}

