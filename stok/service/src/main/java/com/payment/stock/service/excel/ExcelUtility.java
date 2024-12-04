package com.payment.stock.service.excel;

import com.payment.stock.common.enums.UnitType;
import com.payment.stock.entity.dto.StockDto;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@UtilityClass
public class ExcelUtility {
    private final static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private final static String SHEET = "stock";

    public static boolean hasExcelFormat(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public static List<StockDto> excelToDto(Long userId, InputStream is) {
        try {
            List<StockDto> dtoList = new ArrayList<>();
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            int rowNumber = 0;

            while (rows.hasNext()) {
                StockDto dto = new StockDto();
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            dto.setStockName(currentCell.getStringCellValue());
                            break;
                        case 1:
                            dto.setAvailableQuantity(Integer.valueOf(currentCell.getStringCellValue()));
                            break;
                        case 2:
                            dto.setUnitType(UnitType.fromValue(currentCell.getStringCellValue()));
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }

                dto.setUserId(userId);
                dto.setDetails(List.of());
                dtoList.add(dto);
            }
            workbook.close();

            return dtoList;
        } catch (Exception e) {
            throw new RuntimeException("Failed Stock Parse Excel file: " + e.getMessage());
        }
    }
}
