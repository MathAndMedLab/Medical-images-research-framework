package core.algorithm.impl;

import core.Data.Data;
import core.Data.VolumeData;
import core.algorithm.Algorithm;

/**
 * Dummy volume always returns aa constant
 */
public class VolumeAlgorithm implements Algorithm<Data, VolumeData> {
    public VolumeData execute(Data input){
        System.out.println ("Volume algorithm is running");
        return new VolumeData();
    }
}
