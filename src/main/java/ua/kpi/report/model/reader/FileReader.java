package ua.kpi.report.model.reader;

import java.util.List;

public interface FileReader {
    void fillDataByRows();

    List<String> getTitles();

    List<String> getColumnData(int columnNumber);

    List<String> getRowData(int rowNumber);
}
