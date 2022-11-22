package ua.kpi.report.exception;

public class WordWorkerException extends RuntimeException {

    public WordWorkerException() {
        super();
    }

    public WordWorkerException(String message) {
        super(WordWorkerException.class.getName() + message);
    }
}
