package ua.kpi.report.model.writer;

import java.util.List;
import java.util.Map;

public interface FileWriter {
    void fillWordDocument(Map<Integer, List<String>> listOfRowData);

    void createNewWordDocument(String reportFile);
}
