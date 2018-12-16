package features.console;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import core.data.Data;
import core.pipeline.PipelineBlock;

public class CustomPipeline {

    private PipelineBlock initialBlock;

    final MutableGraph<PipelineBlock> blocks;

    public CustomPipeline() {
        this.blocks = GraphBuilder.directed().build();
    }

    public void add(PipelineBlock parent, PipelineBlock newBlock){

        parent.addListener(newBlock);

        blocks.addNode(newBlock);
        blocks.putEdge(parent, newBlock);
    }

    public void add(PipelineBlock newBlock){
        if(initialBlock == null)
            initialBlock = newBlock;

        blocks.addNode(newBlock);
    }

    public void run(Data data) {
        initialBlock.inputDataReady(null, data);
    }
}
