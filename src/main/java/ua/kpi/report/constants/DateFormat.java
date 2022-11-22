package ua.kpi.report.constants;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class DateFormat {
    public static DateTimeFormatter MEDIUM =  DateTimeFormatter
            .ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(new Locale("ru"));
}
