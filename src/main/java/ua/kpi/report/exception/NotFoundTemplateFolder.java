package ua.kpi.report.exception;

public class NotFoundTemplateFolder extends RuntimeException {

    public NotFoundTemplateFolder() {
        super();
    }

    public NotFoundTemplateFolder(String message) {
        super(NotFoundTemplateFiles.class.getName() + message);
    }
}
