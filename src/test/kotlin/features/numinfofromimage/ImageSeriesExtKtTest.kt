package features.numinfofromimage


import core.data.attribute.MirfAttributes
import core.data.attribute.Switch
import features.ij.asImageSeries
import features.nifti.util.Nifti1Reader
import org.junit.Test

class ImageSeriesExtKtTest {

    @Test
    fun checkVolume(){
        val masksPath = javaClass.getResource("/msReport/mask.nii").path
        val masks = Nifti1Reader.read(masksPath).asImageSeries()
        masks.attributes.add(MirfAttributes.THRESHOLDED.new(Switch.get()))

        print(masks.getVolumeInMm3())
        readLine()
    }
}
