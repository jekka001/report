package ua.kpi.report.model.list;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RowList {
    private ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();

    public RowList() {
    }

    public RowList(ObservableList<ObservableList<String>> rows) {
        this.rows = rows;
    }

    public ObservableList<ObservableList<String>> getRows() {
        return rows;
    }
}
