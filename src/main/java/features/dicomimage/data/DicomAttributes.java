package features.dicomimage.data;

import core.data.medimage.AttributeTagType;
import core.data.medimage.DataAttributeMockup;

/**
 * Stores {@link DataAttributeMockup} for all well-known DICOM data attributes
 */
public final class DicomAttributes{


    /**
     * todo: (avlomakin) add link to doc
     */
    public static final DataAttributeMockup ONE_PIXEL_VOLUME = new DataAttributeMockup("onepixelvolume", "(1234, 1234)",
            AttributeTagType.Dicom);

    private DicomAttributes() {}

}

