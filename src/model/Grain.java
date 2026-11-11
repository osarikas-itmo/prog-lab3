package model;

import enums.CropType;
import enums.GrainState;
import java.util.Objects;

public final class Grain {
    private final CropType cropType;
    private double amount;
    private final GrainState state;

    public Grain(CropType cropType, double amount, GrainState state) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("Количество зерна должно быть положительным");
        }
        this.cropType = Objects.requireNonNull(cropType, "Не указана культура");
        this.state = Objects.requireNonNull(state, "Не указано состояние зерна");
        this.amount = amount;
    }

    public CropType getCropType() { return cropType; }
    public double getAmount() { return amount; }
    public GrainState getState() { return state; }

    public void consume() { amount = 0; }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Grain grain)) return false;
        return cropType == grain.cropType && state == grain.state
                && Double.compare(amount, grain.amount) == 0;
    }

    @Override
    public int hashCode() { return Objects.hash(cropType, amount, state); }

    @Override
    public String toString() {
        return String.format(java.util.Locale.ROOT, "%s: %.2f кг (%s)", cropType, amount, state);
    }
}
