package character;

import enums.CropType;
import enums.GrainState;
import enums.ToolType;
import exception.InsufficientGrainException;
import exception.MissingEquipmentException;
import exception.StorageOverflowException;
import farm.Farm;
import farm.Field;
import model.Grain;
import model.YearlyFoodPlan;
import storage.Storage;
import java.util.Objects;

public final class Robinson implements Farmer {
    private final String name;
    private final Farm farm;
    private double consumedGrain;
    private YearlyFoodPlan foodPlan;

    public Robinson(String name, Farm farm) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Не указано имя");
        this.name = name;
        this.farm = Objects.requireNonNull(farm, "Не указана ферма");
    }

    public Farm getFarm() { return farm; }
    public double getConsumedGrain() { return consumedGrain; }
    public YearlyFoodPlan getFoodPlan() { return foodPlan; }

    @Override
    public void sow(Field field, Grain grain) { field.sow(grain); }

    @Override
    public Grain harvest(Field field) throws MissingEquipmentException {
        requireEquipment(ToolType.SICKLE);
        return field.harvest();
    }

    public void transferGrain(Storage source, Storage destination, CropType type,
                              GrainState state, double amount)
            throws InsufficientGrainException, StorageOverflowException {
        if (source == destination) throw new IllegalArgumentException("Нужны разные хранилища");
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("Количество для переноса должно быть положительным");
        }
        if (amount > source.getAmount(type, state)) {
            throw new InsufficientGrainException("В исходном хранилище недостаточно зерна");
        }
        if (amount > destination.getFreeCapacity()) {
            throw new StorageOverflowException("В целевом хранилище недостаточно места");
        }
        destination.store(source.retrieve(type, state, amount));
    }

    public void thresh(Storage storage, CropType type, double amount)
            throws MissingEquipmentException, InsufficientGrainException, StorageOverflowException {
        requireEquipment(ToolType.FLAIL);
        Grain ears = storage.retrieve(type, GrainState.IN_EARS, amount);
        storage.store(new Grain(type, ears.getAmount(), GrainState.THRESHED));
        ears.consume();
    }

    public void consumeGrain(Storage storage, CropType type, double amount)
            throws InsufficientGrainException {
        if (!Double.isFinite(consumedGrain + amount)) {
            throw new IllegalArgumentException("Недопустимое количество пищи");
        }
        Grain food = storage.retrieve(type, GrainState.THRESHED, amount);
        consumedGrain += food.getAmount();
        food.consume();
    }

    public YearlyFoodPlan planYear(double dailyConsumption, double grainToSow) {
        if (!Double.isFinite(dailyConsumption) || dailyConsumption <= 0) {
            throw new IllegalArgumentException("Суточная норма должна быть положительной");
        }
        foodPlan = new YearlyFoodPlan(dailyConsumption * 365, grainToSow);
        return foodPlan;
    }

    private void requireEquipment(ToolType tool) throws MissingEquipmentException {
        if (!farm.hasEquipment(tool)) throw new MissingEquipmentException("Нужен инструмент: " + tool);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Robinson robinson)) return false;
        return name.equals(robinson.name) && farm.equals(robinson.farm)
                && Double.compare(consumedGrain, robinson.consumedGrain) == 0
                && Objects.equals(foodPlan, robinson.foodPlan);
    }

    @Override
    public int hashCode() { return Objects.hash(name, farm, consumedGrain, foodPlan); }

    @Override
    public String toString() {
        return name + ", съедено зерна: " + consumedGrain + " кг, план: " + foodPlan;
    }
}
