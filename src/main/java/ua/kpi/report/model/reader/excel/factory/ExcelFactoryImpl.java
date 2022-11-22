package ua.kpi.report.model.reader.excel.factory;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ua.kpi.report.exception.InvalidExcelTypeException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static ua.kpi.report.constants.ConstantsClass.XLS;
import static ua.kpi.report.constants.ConstantsClass.XLSX;

public class ExcelFactoryImpl implements ExcelFactory {
    @Override
    public Workbook makeWorkBook(File excelFile) throws InvalidExcelTypeException, InvalidFormatException, IOException {
        return getExcelObject(excelFile, excelFile.getName(), "$$$$$ is not excel file - " + excelFile);
    }

    private Workbook getExcelObject(File file, String name, String s) throws IOException, InvalidFormatException, InvalidExcelTypeException {
        if (name.endsWith(XLSX)) {
            return new XSSFWorkbook(file);
        } else if (name.endsWith(XLS)) {
            return new HSSFWorkbook(new FileInputStream(file));
        } else {
            throw new InvalidExcelTypeException(s);
        }
    }
}
