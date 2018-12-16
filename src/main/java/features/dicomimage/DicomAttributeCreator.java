package features.dicomimage;

import core.data.attribute.AttributeCreationException;
import core.data.attribute.DataAttribute;

/**
 * Manages DICOM attributes creation
 */
public final class DicomAttributeCreator {

    private DicomAttributeCreator() {

    }

    /**
     * Creates DICOM data attribute
     * @param tag attribute tag  - (XXXX,XXXX) with hexadecimal numbers
     * @param name attribute name
     * @param value attribute value
     * @return created attribute for Dicom format
     */
    public static DataAttribute createDicomAttribute(String tag, String name, Object value) throws AttributeCreationException {
        if(!isDicomTag(tag))
            throw new AttributeCreationException("invalid tag shape: (XXXX, XXXX) required");

        return new DataAttribute(name, tag, value);
    }


    private static boolean isDicomTag(String tag) {

        if (tag == null || tag.isEmpty())
            return false;

        var pattern = "^[(][0-9a-fA-F]{4}[,][0-9a-fA-F]{4}[)]$";
        return tag.matches(pattern);
    }

}
