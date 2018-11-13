package core.data.medImage;


/**
 * Stores {@link MedImageAttributeMockup} for all well-known MIRF data attributes
 */
public final class MirfAttributes{

    private MirfAttributes() {}

    public static final MedImageAttributeMockup THRESHOLDED = new MedImageAttributeMockup("Thresholded", "96969da6-e6c2-11e8-9f32-f2801f1b9fd1",
            AttributeTagType.UUID, AttributeValueRepresentation.DicomUN);
}