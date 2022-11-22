package ua.kpi.report.exception;

public class InvalidWordTypeException extends Exception {
    public InvalidWordTypeException() {
        super();
    }

    public InvalidWordTypeException(String message) {
        super(InvalidWordTypeException.class.getName() + message);
    }
}