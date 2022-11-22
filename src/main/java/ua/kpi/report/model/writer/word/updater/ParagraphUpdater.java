package ua.kpi.report.model.writer.word.updater;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import ua.kpi.report.model.writer.word.validator.PatternValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;

import static ua.kpi.report.constants.ConstantsClass.*;
import static ua.kpi.report.constants.DateFormat.MEDIUM;
import static ua.kpi.report.constants.SpecialData.*;

public class ParagraphUpdater extends Updater<XWPFParagraph> {
    private int countRowWithSymbol = 0;

    public ParagraphUpdater(Map<Integer, List<String>> listOfRowData) {
        super(listOfRowData);
    }

    @Override
    public void replace(XWPFParagraph paragraph) {
        if (paragraph.getRuns().size() != 0) {
            for (XWPFRun run : paragraph.getRuns()) {
                replaceRun(run);
            }
        }
    }

    private void replaceRun(XWPFRun run) {
        String columnText = run.getText(0);

        if (PatternValidator.isTextHasSpecialSymbol(columnText)) {
            replaceSymbol(run, columnText);
        } else if (isTableHasNumeration(columnText)) {
            increaseNumeration(run, columnText);
        }
    }

    private void replaceSymbol(XWPFRun run, String columnText) {
        int columnNumber = getColumnNumber(columnText);
        String textForReplace = chooseTextForReplace(columnNumber);

        run.setText(columnText.replace(SPECIAL_SYMBOL + columnNumber, textForReplace), 0);
    }

    private String chooseTextForReplace(int columnNumber) {
        if (columnNumber == DATE.getSpecialNumber()) {
            return MEDIUM.format(LocalDateTime.now());
        } else if (columnNumber == PROTOCOL_NUMBER.getSpecialNumber()) {
            return prepareProtocolNumber();
        } else if (columnNumber == CERTIFICATE_NUMBER.getSpecialNumber()) {
            return prepareCertificateNumber();
        } else if (listOfRowData.get(countRowWithSymbol).size() > columnNumber) {
            return listOfRowData.get(countRowWithSymbol).get(columnNumber);
        }

        return "";
    }


    private String prepareCertificateNumber() {
        LocalDateTime today = LocalDateTime.now();
        StringBuilder protocolNumber = new StringBuilder();
        int ordinalNumber = countRowWithSymbol + 1;

        protocolNumber.append(addZeroToNumber(today.getMonthValue(), false));
        protocolNumber.append(" - ");
        protocolNumber.append(addZeroToNumber(ordinalNumber, true));

        return protocolNumber.toString();
    }

    private String prepareProtocolNumber() {
        LocalDateTime today = LocalDateTime.now();
        StringBuilder protocolNumber = new StringBuilder();

        protocolNumber.append(today.getYear() - CENTURY);
        protocolNumber.append(addZeroToNumber(today.getMonthValue(), false));
        protocolNumber.append(" - ").append(new Random().nextInt(RANDOM_BOUND));

        return protocolNumber.toString();
    }

    private String addZeroToNumber(int number, boolean twoZero) {
        StringBuilder numberWithZero = new StringBuilder();

        if (number < 100 && twoZero) {
            numberWithZero.append("0");
        }

        if (number < 10) {
            numberWithZero.append("0").append(number);
        } else {
            numberWithZero.append(number);
        }

        return numberWithZero.toString();
    }

    private int getColumnNumber(String columnText) {
        Matcher matcher = PatternValidator.SPECIAL_SYMBOL.matcher(columnText);

        if (matcher.matches()) {
            return Integer.parseInt(matcher.group("column"));
        }

        return -1;
    }

    private boolean isTableHasNumeration(String columnText) {
        return countRowWithSymbol != 0 && isColumnHasNumeration(columnText);
    }

    private boolean isColumnHasNumeration(String columnText) {
        return columnText.contains("1.");
    }

    private void increaseNumeration(XWPFRun run, String columnText) {
        run.setText(columnText.replace("1.", (countRowWithSymbol + 1) + "."), 0);
    }

    public int getCountRowWithSymbol() {
        return countRowWithSymbol;
    }

    public void setCountRowWithSymbol(int countRowWithSymbol) {
        this.countRowWithSymbol = countRowWithSymbol;
    }
}
