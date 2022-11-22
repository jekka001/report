package ua.kpi.report.exception;

public class InvalidCellTypeException extends RuntimeException {
    public InvalidCellTypeException() {
        super();
    }

    public InvalidCellTypeException(String message) {
        super(InvalidCellTypeException.class.getName() + message);
    }
}