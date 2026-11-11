package enums;

public enum CropType {
    WHEAT("пшеница"), BARLEY("ячмень");

    private final String name;

    CropType(String name) { this.name = name; }

    @Override
    public String toString() { return name; }
}
