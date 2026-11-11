package enums;

public enum FieldState {
    EMPTY("пустое"), GROWING("растёт"), READY_TO_HARVEST("готово к уборке");

    private final String name;

    FieldState(String name) { this.name = name; }

    @Override
    public String toString() { return name; }
}
