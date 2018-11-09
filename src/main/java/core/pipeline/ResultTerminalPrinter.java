package core.pipeline;

import model.data.Data;
import model.pipeline.PipelineBlock;

public class ResultTerminalPrinter extends PipelineBlock<Data, Data> {

    @Override
    public void InputDataReady(PipelineBlock<?, Data> sender, Data data) {
        System.out.println(data.data.toString());
    }
}
