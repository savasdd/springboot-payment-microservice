package com.payment.report.service.impl;

import com.payment.report.common.enums.ReportType;
import com.payment.report.entity.UserDto;
import com.payment.report.service.ReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXmlExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ReportServiceImpl implements ReportService {

    @Override
    public byte[] userJasperReport(String fileType) throws Exception {
        String template = "reports/user_list.jrxml";
        List<UserDto> users = List.of(UserDto.builder().firstName("Savaş").lastName("Dede").email("svsdd@gmail.com").phone("5447884457").build(),
                UserDto.builder().firstName("Selen").lastName("Dede").email("slndd@gmail.com").phone("5447884458").build(),
                UserDto.builder().firstName("Özlem").lastName("Özmen").email("ozlm@gmail.com").phone("5447884459").build());

        Map<String, Object> parameters = new HashMap<>();
        FileInputStream leafBannerStream = new FileInputStream(ResourceUtils.getFile("classpath:reports/jasper.jpg").getAbsolutePath());
        parameters.put("comanyName", "THY TECHNOLOGIES");
        parameters.put("address", "Address: Ankara");
        parameters.put("header", "Users Report");
        parameters.put("logo", leafBannerStream);
        parameters.put("createdBy", "Savaş Dede");

        //2.Create DataSource
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(users);
        //3.Compile .jrmxl template, stored in JasperReport object
        String path = ResourceUtils.getFile("classpath:" + template).getAbsolutePath();
        JasperReport jasperReport = JasperCompileManager.compileReport(path);
        //4.Fill Report - by passing complied .jrxml object, paramters, datasource
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanCollectionDataSource);
        //5.Export Report - by using JasperExportManager
        ReportType reportType = ReportType.fromValue(fileType);
        return exportJasperReportBytes(jasperPrint, reportType);
    }

    private byte[] exportJasperReportBytes(JasperPrint jasperPrint, ReportType reportType) throws JRException {
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
                JRXlsxExporter xlsxExporter = new JRXlsxExporter();
                xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                xlsxExporter.exportReport();
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
                docxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                docxExporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
                docxExporter.exportReport();
                break;
            default:
                // Export to PDF by default
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                break;
        }
        return outputStream.toByteArray();
    }
}
