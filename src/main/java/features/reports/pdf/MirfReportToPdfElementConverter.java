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

public class MirfReportToPdfElementConverter<I extends Data> implements Algorithm<I, PdfElementData> {

    private Algorithm<I, AlgorithmReport> reportCreator;

    public MirfReportToPdfElementConverter(Algorithm<I, AlgorithmReport> reportCreator) {
        this.reportCreator = reportCreator;
    }

    @Override
    public PdfElementData execute(I input) {
        AlgorithmReport report = reportCreator.execute(input);
        return new PdfElementData(execute(report));
    }

    public IBlockElement execute(AlgorithmReport report) {
        IBlockElement generatedReport;

        //TODO: (avlomakin) implement IBlockElement generation for other types of reports
        switch (report.mirfReportType) {
            case DataTable:
                generatedReport = generatePdfForTableReport((DataTableAlgorithmReport) report);
                break;
            case Unknown:
            case Extension:
                throw new RuntimeException(String.format("Cannot generate report for %s", report.mirfReportType));
            default:
                throw new RuntimeException(String.format("Cannot generate report for %s", report.mirfReportType));
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

        Table table = new Table(report.table.columns.size());

        table.setWidth(UnitValue.createPercentValue(70));

        addHeaders(table, report.table.columns, bold);
        for (Dictionary<String, String> row : report.table.rows)
            addRow(table, row, font, report.table.columns);

        return table;
    }

    private void addRow(Table table, Dictionary<String, String> items, PdfFont font, Collection<String> headers) {
        for (String header : headers)
            table.addCell(new Cell().add(new Paragraph(items.get(header)).setFont(font)));
    }

    private void addHeaders(Table table, Collection<String> items, PdfFont font) {
        for (String item : items)
            table.addHeaderCell(new Cell().add(new Paragraph(item).setFont(font)));
    }
}
