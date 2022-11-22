package ua.kpi.report.model.reader.excel.list;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ua.kpi.report.model.list.RowList;
import ua.kpi.report.model.reader.FileReader;
import ua.kpi.report.model.reader.excel.ExcelReader;

import static ua.kpi.report.constants.ConstantsClass.ROW_NUMBER_WITHOUT_TITLES;

public class ExcelRowList {
    private final RowList rowList;
    private final FileReader fileReader;

    public ExcelRowList(FileReader fileReader) {
        this.fileReader = fileReader;
        this.rowList = new RowList();
    }

    public void fillRows() {
        for (int rowNumber = ROW_NUMBER_WITHOUT_TITLES; rowNumber < getCountRows(); rowNumber++) {
            rowList.getRows().add(getExcelRowData(rowNumber));
        }
    }

    private int getCountRows() {
        return ((ExcelReader) fileReader).getDataByRows().size();
    }

    private ObservableList<String> getExcelRowData(int rowNumber) {
        ObservableList<String> row = FXCollections.observableArrayList();

        row.addAll(fileReader.getRowData(rowNumber));

        return row;
    }

    public RowList getRowList() {
        return rowList;
    }
}
