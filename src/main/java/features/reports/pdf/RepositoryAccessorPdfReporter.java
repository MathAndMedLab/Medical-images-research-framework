package features.reports.pdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import core.algorithm.Algorithm;
import core.algorithm.ReportableAlgorithm;
import core.data.report.DataTableReport;
import core.data.report.Report;
import core.pipeline.PipelineBlock;
import core.pipeline.impl.AlgorithmHostBlock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;

/**
 * Common pdf generator for MIRF report types
 */
public class RepositoryAccessorPdfReporter implements Algorithm<AlgorithmHostBlock, FileData> {

    private static final String EXTENSION = ".pdf";
    private String name;

    public RepositoryAccessorPdfReporter(String name) {
        this.name = name;
    }

    @Override
    public FileData execute(AlgorithmHostBlock input) {
        Report report = input.getReport();

        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(resultStream);

        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf, PageSize.A4);

        document.setMargins(10, 10, 10, 10);
        IBlockElement generatedReport;

        switch (report.mirfReportType) {
            case DataTable:
                generatedReport = generatePdfForTableReport((DataTableReport) report);
                break;
            case Unknown:
            case Extension:
                throw new RuntimeException(String.format("Cannot generate report for %s", report.mirfReportType));
            default:
                throw new RuntimeException(String.format("Cannot generate report for %s", report.mirfReportType));
        }

        document.add(generatedReport);
        document.close();
        return new FileData(resultStream.toByteArray(), name, EXTENSION);
    }

    private Table generatePdfForTableReport(DataTableReport report) {

        PdfFont font = null;
        PdfFont bold = null;
        try {
            font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Table table = new Table(report.table.columns.size());

        table.setWidth(UnitValue.createPercentValue(100));

        addHeaders(table, report.table.columns, bold);
        for (Dictionary<String,String> row : report.table.rows)
            addRow(table, row, font, report.table.columns);

        return table;
    }

    private void addRow(Table table, Dictionary<String, String> items, PdfFont font, Collection<String> headers) {
        for (String header: headers)
                table.addCell(new Cell().add(new Paragraph(items.get(header)).setFont(font)));
    }

    private void addHeaders(Table table, Collection<String> items, PdfFont font) {
        for (String item : items)
            table.addHeaderCell(new Cell().add(new Paragraph(item).setFont(font)));
    }
}
