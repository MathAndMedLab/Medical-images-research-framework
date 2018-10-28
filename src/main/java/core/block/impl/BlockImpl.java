package core.block.impl;

import core.Data.Data;
import core.algorithm.Algorithm;
import core.block.Block;

import java.util.ArrayList;

public class BlockImpl<A extends Data, B extends Data> implements Block {
    protected Algorithm<A, B> algo;
    protected A inputData;

    ArrayList<BlockImpl> listeners;

    public BlockImpl(){
        this.listeners = new ArrayList<BlockImpl>();
    }

    public void Execute(){
        B output = algo.execute(inputData);
        notifyListeners(output);
    }

    public void setInputData(A input){
        this.inputData = input;
    }

    public void addListener(BlockImpl listener) {
        this.listeners.add(listener);
    }

    private void notifyListeners(Data output){
        for (BlockImpl listener : listeners){
            listener.setInputData(output);
        }
    }
}
