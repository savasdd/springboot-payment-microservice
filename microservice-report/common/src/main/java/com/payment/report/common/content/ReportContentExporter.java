package com.payment.report.common.content;

import java.io.ByteArrayOutputStream;

import com.payment.report.common.enums.ReportType;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.export.*;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

public class ReportContentExporter {


    public byte[] exportJasperReportBytes(JasperPrint jasperPrint, ReportType reportType, String fileName) throws JRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        switch (reportType) {
            case CSV:
                // Export to CSV
                JRCsvExporter csvExporter = new JRCsvExporter();
                csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                csvExporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
                csvExporter.exportReport();
                break;
            case XLSX:
                // Export to XLSX
                JRXlsxExporter exporter = new JRXlsxExporter();
                jasperPrint.setProperty(JRParameter.IS_IGNORE_PAGINATION, "true");
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                SimpleXlsxReportConfiguration reportConfig = new SimpleXlsxReportConfiguration();
                reportConfig.setSheetNames(new String[]{fileName + " Report"});
                reportConfig.setRemoveEmptySpaceBetweenRows(true);
                reportConfig.setWrapText(false);
                reportConfig.setCollapseRowSpan(true);
                reportConfig.setDetectCellType(true);
                exporter.setConfiguration(reportConfig);
                exporter.exportReport();
                break;
            case XML:
                // Export to XML
                JRXmlExporter xmlExporter = new JRXmlExporter();
                xmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xmlExporter.setExporterOutput(new SimpleXmlExporterOutput(outputStream));
                xmlExporter.exportReport();
                break;
            case DOC:
                // Export to DOCX (RTF format)
                JRRtfExporter docxExporter = new JRRtfExporter();
                jasperPrint.setProperty(JRParameter.IS_IGNORE_PAGINATION, "true");
                docxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                docxExporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
                docxExporter.exportReport();
                break;
            case PDF:
                // Export to PDF (RTF format)
                JRPdfExporter exporterPdf = new JRPdfExporter();
                exporterPdf.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporterPdf.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

                SimplePdfReportConfiguration reportConfigPdf = new SimplePdfReportConfiguration();
                reportConfigPdf.setSizePageToContent(true);
                reportConfigPdf.setForceLineBreakPolicy(false);

                SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
                exportConfig.setEncrypted(true);
                exportConfig.setAllowedPermissionsHint("PRINTING");
                exporterPdf.setConfiguration(reportConfigPdf);
                exporterPdf.setConfiguration(exportConfig);
                exporterPdf.exportReport();
                break;
            default:
                // Export to PDF by default
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                break;
        }
        return outputStream.toByteArray();
    }


}
