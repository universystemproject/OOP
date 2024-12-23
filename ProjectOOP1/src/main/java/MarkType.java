public enum MarkType {
    FIRST_ATTESTATION(1, "first_attestation"),
    SECOND_ATTESTATION(2, "second_attestation"),
    FINAL_EXAM(3, "final_exam");

    private final int value;
    private final String columnName;

    MarkType(int value, String columnName) {
        this.value = value;
        this.columnName = columnName;
    }

    public int getValue() {
        return value;
    }

    public String getColumnName() {
        return columnName;
    }

    public static MarkType fromInt(int value) {
        for (MarkType type : MarkType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }
}
