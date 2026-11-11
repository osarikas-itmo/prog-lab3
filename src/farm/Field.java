package farm;

import enums.CropType;
import enums.FieldState;
import enums.GrainState;
import model.Grain;
import java.util.Objects;

public class Field {
    private CropType cropType;
    private double harvestAmount;
    private final double yieldMultiplier;
    private FieldState state = FieldState.EMPTY;
    private final int growthTime;
    private int elapsedGrowthTime;

    public Field(double yieldMultiplier, int growthTime) {
        if (!Double.isFinite(yieldMultiplier)
                || yieldMultiplier <= 0 || growthTime <= 0) {
            throw new IllegalArgumentException("Урожайность и срок роста должны быть положительными");
        }
        this.yieldMultiplier = yieldMultiplier;
        this.growthTime = growthTime;
    }

    public void sow(Grain grain) {
        if (state != FieldState.EMPTY) {
            throw new IllegalStateException("Поле уже засеяно");
        }
        if (grain == null || grain.getState() == GrainState.IN_EARS
                || grain.getAmount() <= 0) {
            throw new IllegalArgumentException("Для посева нужно зерно без колосьев");
        }
        double expectedHarvest = grain.getAmount() * yieldMultiplier;
        if (!Double.isFinite(expectedHarvest) || expectedHarvest <= 0) {
            throw new IllegalArgumentException("Недопустимое количество урожая");
        }
        cropType = grain.getCropType();
        harvestAmount = expectedHarvest;
        elapsedGrowthTime = 0;
        grain.consume();
        state = FieldState.GROWING;
    }

    public void advanceTime(int days) {
        if (days < 0) {
            throw new IllegalArgumentException("Число дней не может быть отрицательным");
        }
        if (state == FieldState.GROWING) {
            elapsedGrowthTime += Math.min(days, growthTime - elapsedGrowthTime);
            if (elapsedGrowthTime >= growthTime) {
                state = FieldState.READY_TO_HARVEST;
            }
        }
    }

    public Grain harvest() {
        if (state != FieldState.READY_TO_HARVEST) {
            throw new IllegalStateException("Урожай ещё не созрел");
        }
        Grain harvest = new Grain(cropType, harvestAmount, GrainState.IN_EARS);
        cropType = null;
        harvestAmount = 0;
        elapsedGrowthTime = 0;
        state = FieldState.EMPTY;
        return harvest;
    }

    public FieldState getState() {
        return state;
    }

    public CropType getCropType() { return cropType; }
    public double getHarvestAmount() {
        return state == FieldState.READY_TO_HARVEST ? harvestAmount : 0;
    }
    public double getYieldMultiplier() { return yieldMultiplier; }
    public int getGrowthTime() { return growthTime; }
    public int getElapsedGrowthTime() { return elapsedGrowthTime; }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Field field)) return false;
        return cropType == field.cropType && state == field.state
                && Double.compare(harvestAmount, field.harvestAmount) == 0
                && Double.compare(yieldMultiplier, field.yieldMultiplier) == 0
                && growthTime == field.growthTime && elapsedGrowthTime == field.elapsedGrowthTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cropType, harvestAmount, yieldMultiplier, state, growthTime, elapsedGrowthTime);
    }

    @Override
    public String toString() {
        return "Поле: " + state + ", культура: " + (cropType == null ? "нет" : cropType)
                + ", прошло дней: " + elapsedGrowthTime + "/" + growthTime;
    }
}
