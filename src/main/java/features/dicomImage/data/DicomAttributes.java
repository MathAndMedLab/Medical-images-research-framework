package features.dicomImage.data;

import core.data.medImage.AttributeValueRepresentation;

public final class DicomAttributes{

    private DicomAttributes() {}

    public static final DicomAttributeMockup ONE_PIXEL_VOLUME = new DicomAttributeMockup("onepixelvolume", "(1234, 1234)", AttributeValueRepresentation.DicomUN);
}

public class DicomAttributeMockup{
    public final String name;
    public final String tag;
    public final AttributeValueRepresentation valueRepresentation;

    public DicomAttributeMockup(String name, String tag, AttributeValueRepresentation valueRepresentation) { 
        this.name = name;
        this.tag = tag;
        this.valueRepresentation = valueRepresentation;
    }
}