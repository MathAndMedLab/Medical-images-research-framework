package core.pipeline;

import model.data.Data;
import model.pipeline.PipelineBlock;

import java.util.List;

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

