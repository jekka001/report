package ua.kpi.report.model.reader.excel.factory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import ua.kpi.report.exception.InvalidExcelTypeException;

import java.io.File;
import java.io.IOException;

public interface ExcelFactory {
    Workbook makeWorkBook(File excelFile) throws InvalidExcelTypeException, InvalidFormatException, IOException;
}
