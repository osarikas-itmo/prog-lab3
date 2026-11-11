package enums;

public enum ToolType {
    SICKLE("серп"), FLAIL("цеп");

    private final String name;

    ToolType(String name) { this.name = name; }

    @Override
    public String toString() { return name; }
}
