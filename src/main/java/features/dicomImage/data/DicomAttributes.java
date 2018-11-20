package features.dicomImage.data;

import core.data.medImage.AttributeTagType;
import core.data.medImage.MirfAttributeMockup;

/**
 * Stores {@link MirfAttributeMockup} for all well-known DICOM data attributes
 */
public final class DicomAttributes{


    /**
     * todo: (avlomakin) add link to doc
     */
    public static final MirfAttributeMockup ONE_PIXEL_VOLUME = new MirfAttributeMockup("onepixelvolume", "(1234, 1234)",
            AttributeTagType.Dicom);

    private DicomAttributes() {}

}

