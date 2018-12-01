package features.reports;

import com.itextpdf.layout.element.IBlockElement;
import core.data.Data;

/**
 * Host class for {@link IBlockElement} to transmit throughout framework.
 */
public class PdfElementData extends Data {
    public final IBlockElement pdfElement;

    public PdfElementData(IBlockElement pdfElement) {
        super();
        this.pdfElement = pdfElement;
    }
}
