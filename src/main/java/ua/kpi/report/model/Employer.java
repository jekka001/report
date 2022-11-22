package ua.kpi.report.model;

import javafx.collections.ObservableList;

public class Employer {
    private ObservableList<String> fields;

    public Employer(ObservableList<String> fields) {
        this.fields = fields;
    }

    public ObservableList<String> getFields() {
        return fields;
    }

    public void setFields(ObservableList<String> fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(String field : fields){
            stringBuilder.append(field + " ________ ");
        }
        return stringBuilder.toString();
    }
}
