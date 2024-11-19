package com.payment.report.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReportType {
    CSV("CSV"), XLSX("XLSX"), XML("XML"), DOC("DOC"), PDF("PDF");

    private final String reportType;

    ReportType(String reportType) {
        this.reportType = reportType;
    }

    @JsonValue
    public String getContactType() {
        return reportType;
    }

    @JsonCreator
    public static ReportType fromValue(String value) {
        value = value.trim().toUpperCase();
        for (ReportType contact : values()) {
            String currentContact = contact.getContactType();
            if (currentContact.equals(value)) {
                return contact;
            }
        }

        throw new IllegalArgumentException("Invalid value for Contact type Enum: " + value);
    }

    @JsonCreator
    public static String getExtension(String value) {
        value = value.trim().toUpperCase();
        for (ReportType contact : values()) {
            String currentContact = contact.getContactType();
            if (currentContact.equals(value)) {
                return "." + value;
            }
        }

        return value;
    }
}