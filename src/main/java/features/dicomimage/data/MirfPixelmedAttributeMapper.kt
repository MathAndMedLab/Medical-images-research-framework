package features.dicomimage.data

import com.pixelmed.dicom.Attribute
import com.pixelmed.dicom.AttributeTag
import com.pixelmed.dicom.OtherWordAttribute
import com.pixelmed.dicom.TagFromName
import core.array.composeValuesToShortArray
import core.array.expandToByteArray
import core.data.attribute.DataAttribute
import core.data.attribute.DataAttributeMockup
import core.data.attribute.MirfAttributes

object MirfPixelmedAttributeMapper {

    private val mirfToPixelmedPairs : Array<Pair<String, AttributeTag>> = arrayOf(
            Pair(MirfAttributes.IMAGE_ROW_DATA.tag, TagFromName.PixelData)
    )

    private val mirfToPixelmedMap : HashMap<String, AttributeTag> = hashMapOf(*mirfToPixelmedPairs)

    private val pixelmedToMirfMap : HashMap<AttributeTag, String> = hashMapOf(*(mirfToPixelmedPairs.map{Pair(it.second, it.first)}.toTypedArray()))

    fun DataAttributeMockup<*>.toPixelMedForm() : AttributeTag {
        val attributeTag =mirfToPixelmedMap[this.tag]
        checkNotNull(attributeTag) {throw MirfDicomException("No such attribute")}
        return attributeTag
    }

    fun getPixelmedAnalogue(mirfAttributeTag: String ) : AttributeTag {
        val attributeTag = findPixelmedAnalogue(mirfAttributeTag)
        checkNotNull(attributeTag) {throw MirfDicomException("No such attribute")}
        return attributeTag
    }

    fun findPixelmedAnalogue(mirfAttributeTag: String ): AttributeTag?{
        return mirfToPixelmedMap[mirfAttributeTag]
    }

    fun hasPixelmedAnalogue(mirfAttributeTag: String ) = mirfToPixelmedMap[mirfAttributeTag] != null

    fun pixelDataToRowImageData(pixelData: OtherWordAttribute) : DataAttribute<ByteArray>{
        val data = pixelData.shortValues.expandToByteArray()
        return MirfAttributes.IMAGE_ROW_DATA.createAttribute(data)
    }

    fun rowImageDataToPixelData(rowImageData: DataAttribute<ByteArray>) : OtherWordAttribute{
        val data = rowImageData.value.composeValuesToShortArray()
        val attr = OtherWordAttribute(TagFromName.PixelData)
        attr.setValues(data)
        return attr
    }

    fun transofrmPixelmedAttributeToMirf(attr: Attribute) : DataAttribute<*>{
        when(attr.tag){
            TagFromName.PixelData -> return pixelDataToRowImageData(attr as OtherWordAttribute)
            else -> throw MirfDicomException("failed to transform attribute: no transformer found")
        }
    }

    fun transformMirfAttributeToPixelmed(attr: DataAttribute<*>) : Attribute{
        return when (attr.tag){
            MirfAttributes.IMAGE_ROW_DATA.tag -> rowImageDataToPixelData(attr as DataAttribute<ByteArray>)
            else ->  throw MirfDicomException("failed to transform ${attr.name}")
        }
    }
}