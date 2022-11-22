package ua.kpi.report.model.writer.word.updater;

import org.apache.poi.xwpf.usermodel.*;
import  ua.kpi.report.model.writer.word.validator.PatternValidator;
import  ua.kpi.report.exception.WordWorkerException;

import java.util.List;
import java.util.Map;

public class TableUpdater extends Updater<XWPFTable> {
    private final ParagraphUpdater paragraphUpdater;

    public TableUpdater(Map<Integer, List<String>> listOfRowData) {
        super(listOfRowData);
        paragraphUpdater = new ParagraphUpdater(listOfRowData);
    }

    @Override
    public void replace(XWPFTable table) {
        if (isRowsMoreThanOne()) {
            addMoreRowToTable(table);
        }

        replaceRows(table);
    }

    private boolean isRowsMoreThanOne() {
        return listOfRowData.size() > 1;
    }

    private void addMoreRowToTable(XWPFTable table) {
        createNewRow(table);
    }

    private void createNewRow(XWPFTable table) {
        configTable(table);
        XWPFTableRow rootTableRow = getFirstRowTable(table);

        for (int counter = 0; counter < listOfRowData.size() - 1; counter++) {
            createNewRowForPerson(table, rootTableRow);
        }
    }

    private void configTable(XWPFTable table) {
        table.setCellMargins(25, 25, 25, 25);
    }

    private XWPFTableRow getFirstRowTable(XWPFTable table) {
        for (XWPFTableRow row : table.getRows()) {
            if (isRowWithSpecialSymbol(row)) {
                return row;
            }
        }

        throw new WordWorkerException("$$$$ Row with special Symbol don't find $$$$");
    }

    private boolean isRowWithSpecialSymbol(XWPFTableRow row) {
        for (XWPFTableCell cell : row.getTableCells()) {
            for (XWPFParagraph paragraph : cell.getParagraphs()) {
                if (isParagraphHasSpecialSymbol(paragraph)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isParagraphHasSpecialSymbol(XWPFParagraph paragraph) {
        return paragraph.getRuns().size() != 0 &&
                (PatternValidator.isTextHasSpecialSymbol(paragraph.getRuns().get(0).getText(0)) ||
                        PatternValidator.isTextHasNumerationSymbol(paragraph.getRuns().get(0).getText(0)));
    }

    private void createNewRowForPerson(XWPFTable table, XWPFTableRow rootTableRow) {
        int posRowInTable = table.getRows().indexOf(rootTableRow);
        XWPFTableRow newTableRow = table.insertNewTableRow(posRowInTable + 1);
        configRow(rootTableRow, newTableRow);

        createNewCells(table, rootTableRow, posRowInTable, newTableRow);
    }

    private void configRow(XWPFTableRow rootTableRow, XWPFTableRow newTableRow) {
        newTableRow.setHeight(rootTableRow.getHeight());
    }

    private void createNewCells(XWPFTable table, XWPFTableRow rootTableRow, int posRowInTable, XWPFTableRow newTableRow) {
        int countCellsInRow = table.getRows().get(posRowInTable).getTableICells().size();

        for (int currentCellIndex = 0; currentCellIndex < countCellsInRow; currentCellIndex++) {
            XWPFTableCell rootCell = rootTableRow.getCell(currentCellIndex);
            XWPFTableCell newCell = createNewCell(rootCell, newTableRow);

            createNewParagraphs(rootCell, newCell);
        }
    }

    private XWPFTableCell createNewCell(XWPFTableCell rootTableCell, XWPFTableRow newTableRow) {
        XWPFTableCell newTableCell = newTableRow.addNewTableCell();

        configCell(rootTableCell, newTableCell);

        return newTableCell;
    }

    private void configCell(XWPFTableCell rootTableCell, XWPFTableCell newTableCell) {
        newTableCell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        newTableCell.getCTTc().addNewTcPr().addNewTcBorders();
        newTableCell.getCTTc().getTcPr().setTcBorders(
                rootTableCell.getCTTc().getTcPr().getTcBorders());
    }

    private void createNewParagraphs(XWPFTableCell rootTableCell, XWPFTableCell newTableCell) {
        XWPFParagraph rootParagraph = rootTableCell.getParagraphs().get(0);
        XWPFParagraph newParagraph = createNewParagraph(newTableCell);

        if (rootParagraph.getRuns().size() != 0) {
            createNewRun(rootParagraph, newParagraph);
        }
    }

    private XWPFParagraph createNewParagraph(XWPFTableCell newTableCell) {
        XWPFParagraph newCellParagraph = newTableCell.addParagraph();

        newCellParagraph.setAlignment(ParagraphAlignment.CENTER);

        return newCellParagraph;
    }

    private void createNewRun(XWPFParagraph rootParagraph, XWPFParagraph newParagraph) {
        XWPFRun rootRun = rootParagraph.getRuns().get(0);
        XWPFRun newRun = newParagraph.createRun();

        configRun(rootRun, newRun);
    }

    private void configRun(XWPFRun rootRun, XWPFRun newRun) {
        newRun.setBold(rootRun.isBold());
        newRun.setItalic(rootRun.isItalic());
        newRun.setFontFamily(rootRun.getFontFamily());
        newRun.setText(rootRun.getText(0));

        if (rootRun.getFontSizeAsDouble() != null)
            newRun.setFontSize(rootRun.getFontSizeAsDouble());
    }

    private void replaceRows(XWPFTable table) {
        int countRowWithSymbol = 0;

        for (XWPFTableRow rowWithSymbol : table.getRows()) {
            if (isRowWithSpecialSymbol(rowWithSymbol)) {
                replaceRow(rowWithSymbol, countRowWithSymbol);
                if (listOfRowData.size() > 1)
                    countRowWithSymbol++;
            }
        }
    }

    private void replaceRow(XWPFTableRow rowWithSymbol, int countRowWithSymbol) {
        for (XWPFTableCell cell : rowWithSymbol.getTableCells()) {
            replaceCell(cell, countRowWithSymbol);
        }
    }

    private void replaceCell(XWPFTableCell cell, int countRowWithSymbol) {
        for (XWPFParagraph paragraph : cell.getParagraphs()) {
            if (isParagraphHasSpecialSymbol(paragraph)) {
                paragraphUpdater.setCountRowWithSymbol(countRowWithSymbol);
                paragraphUpdater.replace(paragraph);
            }
        }
    }
}
