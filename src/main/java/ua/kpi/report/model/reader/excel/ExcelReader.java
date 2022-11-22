package ua.kpi.report.model.reader.excel;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import ua.kpi.report.exception.ExcelWorkerException;
import ua.kpi.report.exception.InvalidCellTypeException;
import ua.kpi.report.exception.InvalidExcelTypeException;
import ua.kpi.report.model.reader.FileReader;
import ua.kpi.report.model.reader.excel.factory.ExcelFactory;
import ua.kpi.report.model.reader.excel.factory.ExcelFactoryImpl;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ua.kpi.report.constants.ConstantsClass.NUMBER_FIRST_SHEET;
import static ua.kpi.report.constants.DateFormat.MEDIUM;

public class ExcelReader implements FileReader {
    private final Map<Integer, List<Cell>> dataByRows = new HashMap<>();
    private Workbook excelBook;

    public ExcelReader(String pathExcel) {
        try {
            readExcelByName(pathExcel);
        } catch (InvalidExcelTypeException | InvalidFormatException | IOException e) {
            throw new ExcelWorkerException("$$$$$ excel file - " + pathExcel + " is not found $$$$$");
        }
    }

    public ExcelReader(File excelFile) {
        try {
            readExcelByFile(excelFile);
        } catch (InvalidExcelTypeException | InvalidFormatException | IOException e) {
            throw new ExcelWorkerException("$$$$$ excel file - " + excelFile + " is not found $$$$$");
        }
    }

    private void readExcelByName(String pathExcel) throws InvalidExcelTypeException, IOException, InvalidFormatException {
        File excelFile = new File(pathExcel);
        readExcel(excelFile);
    }

    private void readExcelByFile(File excelFile) throws InvalidExcelTypeException, IOException, InvalidFormatException {
        readExcel(excelFile);
    }

    private void readExcel(File excelFile) throws InvalidExcelTypeException, InvalidFormatException, IOException {
        ExcelFactory excelFactory = new ExcelFactoryImpl();

        excelBook = excelFactory.makeWorkBook(excelFile);

        fillDataByRows();
    }

    @Override
    public void fillDataByRows() {
        int rowNumber = 0;
        Sheet sheet = excelBook.getSheetAt(NUMBER_FIRST_SHEET);

        for (Iterator<Row> rowIterator = sheet.rowIterator(); rowIterator.hasNext(); ) {
            List<Cell> cellsOfRow = getListOfCells(rowIterator.next());

            dataByRows.put(rowNumber++, cellsOfRow);
        }
    }

    private List<Cell> getListOfCells(Row row) {
        List<Cell> cellsOfRow = new ArrayList<>();

        for (Iterator<Cell> cellIterator = row.cellIterator(); cellIterator.hasNext(); ) {
            cellsOfRow.add(cellIterator.next());
        }

        return cellsOfRow;
    }

    @Override
    public List<String> getTitles() {
        return dataByRows.get(0).stream().map(this::parseCellData).collect(Collectors.toList());
    }

    @Override
    public List<String> getColumnData(int columnNumber) {
        List<String> cellsOfColumn = new ArrayList<>();

        dataByRows.values()
                .stream()
                .skip(1)
                .filter(row -> row.size() > 0)
                .forEach(row -> cellsOfColumn.add(parseCellData(row.get(columnNumber))));

        return cellsOfColumn;
    }

    @Override
    public List<String> getRowData(int rowNumber) {
        List<String> cellsOfRow = new ArrayList<>();

        dataByRows.get(rowNumber)
                .forEach(cell -> cellsOfRow.add(parseCellData(cell)));

        return cellsOfRow;
    }

    private String parseCellData(Cell cell) {
        if (cell.getCellType().equals(CellType.STRING)) {
            return cell.getStringCellValue();
        } else if (cell.getCellType().equals(CellType.NUMERIC)) {
            return parseNumericType(cell);
        } else if (cell.getCellType().equals(CellType.BLANK)) {
            return "";
        }
        throw new InvalidCellTypeException("$$$$ cell type is not supported " + cell.getCellType());
    }

    private String parseNumericType(Cell cell) {
        if (DateUtil.isCellDateFormatted(cell)) {
            return getFormatDate(cell);
        } else {
            return String.valueOf(cell.getNumericCellValue());
        }
    }

    private String getFormatDate(Cell cell) {
        return MEDIUM.format(cell.getLocalDateTimeCellValue().toLocalDate());
    }

    public Map<Integer, List<Cell>> getDataByRows() {
        return dataByRows;
    }
}
