package core.block.impl;

import core.Data.Data;
import core.Data.VolumeData;
import core.algorithm.impl.VolumeAlgorithm;

public class VolumeBlock extends BlockImpl<Data, VolumeData> {
    public VolumeBlock(){
        super();
        this.algo = new VolumeAlgorithm();
    }
}