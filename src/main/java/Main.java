import core.Data.DirData;
import core.algorithm.Algorithm;
import core.algorithm.impl.DicomReaderAlgorithm;
import core.block.Block;
import core.block.impl.BlockImpl;
import core.block.impl.DicomReaderBlock;
import core.block.impl.VolumeBlock;

import java.util.ArrayList;

public class Main {
    public static void main (String args []) {
        DicomReaderBlock dicomReader = new DicomReaderBlock();
        VolumeBlock volumeCalculator = new VolumeBlock();
        dicomReader.setInputData(new DirData());
        dicomReader.addListener(volumeCalculator);

        ArrayList<Block> pipe = new ArrayList<Block>();
        pipe.add(dicomReader);
        pipe.add(volumeCalculator);
        // Pipeline mock, runs consequantly blocks.
        // May be improved with custom Event class to handle if we need to fire execute on some class's side.
        for (Block block : pipe){
            block.Execute();
        }
        System.out.println ("Pipeline finished");
    }
}
