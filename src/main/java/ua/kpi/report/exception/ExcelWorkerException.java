package ua.kpi.report.exception;

public class ExcelWorkerException extends RuntimeException {
    public ExcelWorkerException() {
        super();
    }

    public ExcelWorkerException(String message) {
        super(ExcelWorkerException.class.getName() + message);
    }
}
