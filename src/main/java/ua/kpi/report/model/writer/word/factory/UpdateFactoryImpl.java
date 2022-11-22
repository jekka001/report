package ua.kpi.report.model.writer.word.factory;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import ua.kpi.report.model.writer.word.updater.ParagraphUpdater;
import ua.kpi.report.model.writer.word.updater.TableUpdater;
import ua.kpi.report.model.writer.word.updater.Updater;
import ua.kpi.report.exception.UpdaterTypeException;

import java.util.List;
import java.util.Map;

import static ua.kpi.report.constants.ConstantsClass.DOCUMENT_HAS_NOT_SUPPORTED_TYPE;

public class UpdateFactoryImpl implements UpdateFactory {

    @Override
    public Updater makeUpdater(IBodyElement bodyElement, Map<Integer, List<String>> listOfRowData) {
        if (bodyElement instanceof XWPFTable) {
            return new TableUpdater(listOfRowData);
        } else if (bodyElement instanceof XWPFParagraph) {
            return new ParagraphUpdater(listOfRowData);
        } else {
            throw new UpdaterTypeException(DOCUMENT_HAS_NOT_SUPPORTED_TYPE);
        }
    }
}
