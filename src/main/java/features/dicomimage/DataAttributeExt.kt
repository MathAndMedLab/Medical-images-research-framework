package features.dicomimage

import com.pixelmed.dicom.Attribute
import core.data.attribute.DataAttribute
import features.dicomimage.data.MirfPixelmedAttributeMapper

fun DataAttribute<*>.hasPixelmedAnalogue() : Boolean{
    return MirfPixelmedAttributeMapper.hasPixelmedAnalogue(this.tag)
}

fun DataAttribute<*>.getPixelmedAnalogue() : Attribute{
    return MirfPixelmedAttributeMapper.transformMirfAttributeToPixelmed(this)
}

