package com.payment.report.common.content;

import com.payment.report.common.config.JasperConfig;
import com.payment.report.common.enums.ReportType;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ReportContentService {

    public <T> ResponseEntity<Resource> download(String fileType, String fileName, List<T> dataSource, Map<String, Object> parameters) throws Exception {
        byte[] bytes = export(fileType, fileName.toUpperCase(), dataSource, parameters);
        if (null != bytes) {
            ByteArrayResource resource = new ByteArrayResource(bytes);
            fileName = fileName.toUpperCase() + "_" + new SimpleDateFormat("yyyy-MM-dd:HH:mm").format(new Date()) + ReportType.getExtension(fileType);

            log.info("Generate Report : {}", fileName);
            return ResponseEntity.ok().header(com.google.common.net.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").contentLength(resource.contentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
        } else {
            throw new Exception("File Download Failed");
        }
    }

    private <T> byte[] export(String fileType, String fileName, List<T> dataSource, Map<String, Object> parameters) {
        byte[] bytes = null;

        try {
            log.info("Params:{}", parameters);
            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.register(JasperConfig.class);
            ctx.refresh();

            ReportContentFilter filter = ctx.getBean(ReportContentFilter.class);
            //filter.setReportFileName(raporAdi + ".jasper");
            //filter.loadReport();

            filter.setFileName(fileName + ".jrxml");
            filter.compileReport();

            filter.setParameters(parameters);
            filter.fillReport(dataSource);

            bytes = new ReportContentExporter().exportJasperReportBytes(filter.getJasperPrint(), ReportType.fromValue(fileType), fileName);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Export Report Failed: {}", e.getMessage());
        }

        return bytes;
    }

    private <T> byte[] byteJasperReport(String fileType, String fileName, List<T> dataSource, Map<String, Object> parameters) throws Exception {
        String template = "reports/" + fileName + ".jrxml";
        FileInputStream leafBannerStream = new FileInputStream(ResourceUtils.getFile("classpath:reports/jasper.png").getAbsolutePath());
        parameters.put("logo", leafBannerStream);

        //2.Create DataSource
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dataSource);
        String path = ResourceUtils.getFile("classpath:" + template).getAbsolutePath();
        JasperReport jasperReport = JasperCompileManager.compileReport(path);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanCollectionDataSource);
        ReportType reportType = ReportType.fromValue(fileType);
        return new ReportContentExporter().exportJasperReportBytes(jasperPrint, reportType, fileName);
    }


}
