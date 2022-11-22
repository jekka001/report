package ua.kpi.report.constants;

public enum SpecialData {
    DATE(66),
    PROTOCOL_NUMBER(99),
    CERTIFICATE_NUMBER(98);


    private int specialNumber;

    SpecialData(int specialNumber) {
        this.specialNumber = specialNumber;
    }

    public int getSpecialNumber() {
        return specialNumber;
    }

    public void setSpecialNumber(int specialNumber) {
        this.specialNumber = specialNumber;
    }
}
