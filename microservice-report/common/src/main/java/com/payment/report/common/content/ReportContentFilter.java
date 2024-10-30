package com.payment.report.common.content;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Data;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.JasperCompileManager;

@Data
@Component
public class ReportContentFilter {

    private String fileName;

    private JasperReport jasperReport;

    private JasperPrint jasperPrint;

    private Map<String, Object> parameters;

    public ReportContentFilter() {
        parameters = new HashMap<>();
    }

    public void prepareReport() throws Exception {
        compileReport();
        fillReport(List.of());
    }

    public void compileReport() throws Exception {
        try {
            InputStream reportStream = getClass().getResourceAsStream("/".concat(fileName));
            jasperReport = JasperCompileManager.compileReport(reportStream);
            JRSaver.saveObject(jasperReport, fileName.replace(".jrxml", ".jasper"));

        } catch (JRException ex) {
            ex.printStackTrace();
            Logger.getLogger(ReportContentFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadReport() {
        try {
            InputStream jasperStream = getClass().getResourceAsStream("/".concat(fileName));
            jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        } catch (JRException ex) {
            ex.printStackTrace();
            Logger.getLogger(ReportContentFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public <T> void fillReport(List<T> dataSource) {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dataSource);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanCollectionDataSource);
        } catch (JRException ex) {
            Logger.getLogger(ReportContentFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
