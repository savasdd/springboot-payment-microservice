package com.payment.report.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity<Resource> downloadUserReport(String fileType, String fileName) throws Exception;
}

