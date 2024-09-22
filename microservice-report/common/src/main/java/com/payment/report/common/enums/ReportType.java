package com.payment.report.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReportType {
    CSV("CSV"), XLSX("XLSX"), XML("XML"), DOC("DOC");

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
        for (ReportType contact : values()) {
            String currentContact = contact.getContactType();
            if (currentContact.equals(value)) {
                return contact;
            }
        }

        throw new IllegalArgumentException("Invalid value for Contact type Enum: " + value);
    }
}
