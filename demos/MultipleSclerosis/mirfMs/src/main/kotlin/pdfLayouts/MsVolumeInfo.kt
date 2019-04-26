package pdfLayouts

import com.mirf.core.common.VolumeValue

data class MsVolumeInfo(val totalVolume: VolumeValue, val activeVolume: VolumeValue){

    fun getDiffPerc(other: MsVolumeInfo): Pair<Double, Double>{
        return Pair((this.totalVolume.value / other.totalVolume.value) * 100, (this.activeVolume.value / other.activeVolume.value) * 100)
    }
}
