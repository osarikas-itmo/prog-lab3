package enums;

public enum GrainState {
    SEED("семена"), IN_EARS("в колосьях"), THRESHED("обмолочено");

    private final String name;

    GrainState(String name) { this.name = name; }

    @Override
    public String toString() { return name; }
}
