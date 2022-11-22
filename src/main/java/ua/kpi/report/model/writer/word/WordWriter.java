package ua.kpi.report.model.writer.word;

import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import ua.kpi.report.exception.InvalidWordTypeException;
import ua.kpi.report.exception.WordWorkerException;
import ua.kpi.report.model.writer.FileWriter;
import ua.kpi.report.model.writer.word.factory.UpdateFactory;
import ua.kpi.report.model.writer.word.factory.UpdateFactoryImpl;
import ua.kpi.report.model.writer.word.factory.WordFactory;
import ua.kpi.report.model.writer.word.factory.WordFactoryImpl;
import ua.kpi.report.model.writer.word.updater.Updater;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WordWriter implements FileWriter {
    private Updater updater;
    private Document document;

    public WordWriter(String templateFile) {
        try {
            initDocument(templateFile);
        } catch (InvalidWordTypeException | IOException e) {
            throw new WordWorkerException("$$$$$ word file - " + templateFile + " is not found $$$$$");
        }
    }

    private void initDocument(String templateFile) throws IOException, InvalidWordTypeException {
        WordFactory wordFactory = new WordFactoryImpl();

        document = wordFactory.makeDocument(templateFile);
    }

    @Override
    public void fillWordDocument(Map<Integer, List<String>> listOfRowData) {
        for (IBodyElement bodyElement : ((XWPFDocument) document).getBodyElements()) {
            parseBodyElement(bodyElement, listOfRowData);
        }
    }

    private void parseBodyElement(IBodyElement bodyElement, Map<Integer, List<String>> listOfRowData) {
        UpdateFactory updateFactory = new UpdateFactoryImpl();

        updater = updateFactory.makeUpdater(bodyElement, listOfRowData);

        updater.replace(bodyElement);
    }

    @Override
    public void createNewWordDocument(String reportFile) {
        try {
            FileOutputStream outputStream = new FileOutputStream(reportFile);

            ((XWPFDocument) document).write(outputStream);

            outputStream.close();
            ((XWPFDocument) document).close();
        } catch (IOException e) {
            throw new WordWorkerException("$$$$ create word file " + reportFile + " is exception $$$$");
        }
    }
}