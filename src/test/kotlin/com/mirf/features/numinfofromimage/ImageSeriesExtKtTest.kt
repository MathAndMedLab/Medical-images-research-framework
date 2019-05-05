package com.mirf.features.numinfofromimage


import com.mirf.core.data.attribute.MirfAttributes
import com.mirf.core.data.attribute.Switch
import com.mirf.features.ij.asImageSeries
import com.mirf.features.nifti.util.Nifti1Reader
import org.junit.Test

class ImageSeriesExtKtTest {

    @Test
    fun checkVolume(){
        val res = javaClass.getResource("/mask.raw") ?: return
        val masksPath = javaClass.getResource("/msReport/mask.nii") ?: return
        val masks = Nifti1Reader.read(masksPath.path).asImageSeries()
        masks.attributes.add(MirfAttributes.THRESHOLDED.new(Unit))

        print(masks.getVolume())
    }
}
