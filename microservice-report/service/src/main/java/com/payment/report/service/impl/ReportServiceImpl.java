package com.payment.report.service.impl;

import com.payment.report.common.content.ReportContentService;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ReportServiceImpl implements ReportService {

    private final ReportContentService contentService;

    @Override
    public ResponseEntity<Resource> downloadUserReport(String fileType, String fileName) throws Exception {
        List<UserDto> users = List.of(UserDto.builder().firstName("Savaş").lastName("Dede").email("svsdd@gmail.com").phone("5447884457").build(),
                UserDto.builder().firstName("Selen").lastName("Dede").email("slndd@gmail.com").phone("5447884458").build(),
                UserDto.builder().firstName("Özlem").lastName("Özmen").email("ozlm@gmail.com").phone("5447884459").build());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("comanyName", "THY TECHNOLOGIES");
        parameters.put("address", "Ankara");
        parameters.put("header", "Users Report");
        parameters.put("createdBy", "Savaş Dede");

        return contentService.download(fileType, fileName, new ArrayList<UserDto>(users), parameters);
    }


}
