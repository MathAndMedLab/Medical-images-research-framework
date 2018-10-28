package core.block.impl;

import core.Data.DicomImage;
import core.Data.DirData;
import core.algorithm.impl.DicomReaderAlgorithm;

public class DicomReaderBlock extends BlockImpl<DirData, DicomImage> {
    public DicomReaderBlock(){
        super();
        this.algo = new DicomReaderAlgorithm();
    }
}
