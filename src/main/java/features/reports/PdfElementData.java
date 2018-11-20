package features.reports;

import com.itextpdf.layout.element.IBlockElement;
import core.data.Data;

public class PdfElementData extends Data {
    public final IBlockElement pdfElement;

    public PdfElementData(IBlockElement pdfElement) {
        super();
        this.pdfElement = pdfElement;
    }
}
