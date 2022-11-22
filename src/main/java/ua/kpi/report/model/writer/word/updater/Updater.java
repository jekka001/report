package ua.kpi.report.model.writer.word.updater;

import org.apache.poi.xwpf.usermodel.IBodyElement;

import java.util.List;
import java.util.Map;

public abstract class Updater<T extends IBodyElement> {
    protected final Map<Integer, List<String>> listOfRowData;

    public Updater(Map<Integer, List<String>> listOfRowData) {
        this.listOfRowData = listOfRowData;
    }

    public abstract void replace(T replaceElement);
}
