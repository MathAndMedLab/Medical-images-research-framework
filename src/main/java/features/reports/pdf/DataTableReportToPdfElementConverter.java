package features.reports.pdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import core.algorithm.Algorithm;
import core.data.Data;
import core.data.report.AlgorithmReport;
import core.data.report.DataTableAlgorithmReport;
import features.reports.PdfElementData;

import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;

/**
 * {@link Algorithm} that decorates report creator and generates {@link PdfElementData} from decorated {@link Algorithm} output
 * @param <I> Inner {@link Algorithm} input
 */
public class DataTableReportToPdfElementConverter<I extends Data> implements Algorithm<I, PdfElementData> {

    private Algorithm<I, AlgorithmReport> reportCreator;

    public DataTableReportToPdfElementConverter(Algorithm<I, AlgorithmReport> reportCreator) {
        this.reportCreator = reportCreator;
    }

    @Override
    public PdfElementData execute(I input) {
        var report = reportCreator.execute(input);
        return new PdfElementData(execute(report));
    }

    private IBlockElement execute(AlgorithmReport report) {
        IBlockElement generatedReport;

        switch (report.getMirfReportType()) {
            case DataTable:
                generatedReport = generatePdfForTableReport((DataTableAlgorithmReport) report);
                break;
            default:
                throw new RuntimeException(String.format("Cannot generate report for %s", report.getMirfReportType()));
        }
        return generatedReport;
    }

    private Table generatePdfForTableReport(DataTableAlgorithmReport report) {

        PdfFont font = null;
        PdfFont bold = null;
        try {
            font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        } catch (IOException e) {
            e.printStackTrace();
        }

        var table = new Table(report.getTable().getColumns().size());

        table.setWidth(UnitValue.createPercentValue(100));

        addHeaders(table, report.getTable().getColumns(), bold);
        for (var row : report.getTable().getRows())
            addRow(table, row, font, report.getTable().getColumns());

        return table;
    }

    private void addRow(Table table, Dictionary<String, String> items, PdfFont font, Collection<String> headers) {
        for (var header : headers)
            table.addCell(new Cell().add(new Paragraph(items.get(header)).setFont(font)));
    }

    private void addHeaders(Table table, Collection<String> items, PdfFont font) {
        for (var item : items)
            table.addHeaderCell(new Cell().add(new Paragraph(item).setFont(font)));
    }
}
