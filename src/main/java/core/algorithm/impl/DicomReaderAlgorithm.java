package core.algorithm.impl;

import core.Data.DicomImage;
import core.Data.DirData;
import core.algorithm.Algorithm;

/**
 * Receives a String with the directory and outputs {@link DicomImage}
 */
public class DicomReaderAlgorithm implements Algorithm<DirData, DicomImage> {

    public DicomImage execute(DirData input){
        System.out.println ("DicomReaderAlgorithm is running");
        return new DicomImage();
    }
}
