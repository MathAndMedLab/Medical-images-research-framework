package core.pipeline.impl;

import core.pipeline.PipelineBlock;
import core.data.Data;

//TODO(avlomakin): add javadoc here
public class ResultTerminalPrinter extends PipelineBlock<Data, Data> {

    @Override
    public void InputDataReady(PipelineBlock<?, Data> sender, Data data) {
        System.out.println(data.data.toString());
    }
}
