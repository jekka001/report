package ua.kpi.report.model.writer.word.factory;

import org.apache.poi.xwpf.usermodel.Document;
import ua.kpi.report.exception.InvalidWordTypeException;

import java.io.IOException;

public interface WordFactory {
    Document makeDocument(String templateWordFile) throws InvalidWordTypeException, IOException;
}
