package pdfLayouts

import com.mirf.core.common.VolumeValue
import com.mirf.core.data.AttributeCollection
import com.mirf.core.data.Data

data class MsVolumeInfo(val totalVolume: VolumeValue, val activeVolume: VolumeValue,
                        override val attributes: AttributeCollection = AttributeCollection()) : Data {

    fun getDiffPerc(other: MsVolumeInfo): Pair<Double, Double>{
        return Pair((this.totalVolume.value / other.totalVolume.value) * 100 - 100, (this.activeVolume.value / other.activeVolume.value) * 100 - 100)
    }
}
