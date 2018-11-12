package core.data.medImage;

/*
Stores all mockups for all well-known MIRF data attrtibutes
*/
public final class MirfAttributes{

    private DicomAttributes() {}

    public static final MedImageAttributeMockup ONE_PIXEL_VOLUME = new MedImageAttributeMockup("onepixelvolume", "(1234, 1234)", AttributeValueRepresentation.DicomUN);
}