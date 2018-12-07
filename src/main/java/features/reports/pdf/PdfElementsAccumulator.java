package features.reports.pdf;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import core.algorithm.Algorithm;
import core.data.CollectionData;
import core.data.FileData;
import features.reports.PdfElementData;

import java.io.ByteArrayOutputStream;

/**
 * {@link Algorithm} that puts all {@link PdfElementData} from input to PDF document and returns {@link FileData} with generated PDF
 */
public class PdfElementsAccumulator implements Algorithm<CollectionData<PdfElementData>, FileData> {

    private static final String EXTENSION = ".pdf";
    private String reportName;

    public PdfElementsAccumulator(String reportName) {
        this.reportName = reportName;
    }

    @Override
    public FileData execute(CollectionData<PdfElementData> elements) {
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(resultStream);

        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf, PageSize.A4);

        document.setMargins(10, 10, 10, 10);

        for (PdfElementData element : elements.collection)
            document.add(element.pdfElement);

        document.close();
        return new FileData(resultStream.toByteArray(), reportName, EXTENSION);
    }
}
