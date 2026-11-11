package enums;

public enum CropType {
    RICE("рис"), BARLEY("ячмень");

    private final String name;

    CropType(String name) { this.name = name; }

    @Override
    public String toString() { return name; }
}
