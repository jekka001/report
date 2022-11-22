package ua.kpi.report.model.writer.word.factory;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import  ua.kpi.report.model.writer.word.updater.Updater;

import java.util.List;
import java.util.Map;

public interface UpdateFactory {
    Updater makeUpdater(IBodyElement bodyElement, Map<Integer, List<String>> listOfRowData);
}
