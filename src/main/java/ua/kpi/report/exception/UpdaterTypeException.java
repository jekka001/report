package ua.kpi.report.exception;

public class UpdaterTypeException extends RuntimeException {

    public UpdaterTypeException() {
        super();
    }

    public UpdaterTypeException(String message) {
        super(UpdaterTypeException.class.getName() + message);
    }
}
