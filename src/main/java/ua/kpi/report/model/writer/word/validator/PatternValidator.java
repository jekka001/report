package ua.kpi.report.model.writer.word.validator;

import java.util.regex.Pattern;

public class PatternValidator {
    public static final Pattern SPECIAL_SYMBOL = Pattern.compile(".*\\$(?<column>[0-9]?[0-9]).*");
    public static final Pattern NUMERATION_SYMBOL = Pattern.compile("^1\\.$");

    public static boolean isTextHasSpecialSymbol(String columnText) {
        return columnText != null && SPECIAL_SYMBOL.matcher(columnText).matches();
    }

    public static boolean isTextHasNumerationSymbol(String columnText){
        return columnText != null && NUMERATION_SYMBOL.matcher(columnText).matches();
    }
}
