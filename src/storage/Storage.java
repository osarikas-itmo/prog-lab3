package storage;

import enums.CropType;
import enums.GrainState;
import exception.InsufficientGrainException;
import exception.StorageOverflowException;
import model.Grain;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Storage {
    private final String name;
    private final double capacity;
    private final List<Grain> grain = new ArrayList<>();

    protected Storage(String name, double capacity) {
        if (name == null || name.isBlank() || !Double.isFinite(capacity) || capacity <= 0) {
            throw new IllegalArgumentException("Нужны название и положительная вместимость хранилища");
        }
        this.name = name;
        this.capacity = capacity;
    }

    public String getName() { return name; }
    public abstract String getStorageType();
    public double getCapacity() { return capacity; }
    public double getAmount() { return grain.stream().mapToDouble(g -> g.getAmount()).sum(); }
    public double getFreeCapacity() { return capacity - getAmount(); }

    public double getAmount(CropType type, GrainState state) {
        return grain.stream().filter(g -> g.getCropType() == type && g.getState() == state)
                .mapToDouble(Grain::getAmount).sum();
    }
    public List<Grain> getContents() {
        return List.copyOf(grain);
    }

    public void store(Grain incoming) throws StorageOverflowException {
        Objects.requireNonNull(incoming, "Не указано зерно");
        if (incoming.getAmount() <= 0) {
            throw new IllegalArgumentException("Нельзя положить пустую порцию зерна");
        }
        if (incoming.getAmount() > getFreeCapacity()) {
            throw new StorageOverflowException("Недостаточно места: " + name);
        }
        grain.add(new Grain(incoming.getCropType(), incoming.getAmount(), incoming.getState()));
        incoming.consume();
    }

    public Grain retrieve(CropType type, GrainState state, double amount)
            throws InsufficientGrainException {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("Количество для выдачи должно быть положительным");
        }
        if (amount > getAmount(type, state)) {
            throw new InsufficientGrainException("Недостаточно зерна: " + name + ", " + type + ", " + state);
        }
        double remaining = amount;
        for (int i = 0; i < grain.size() && remaining > 0;) {
            Grain portion = grain.get(i);
            if (portion.getCropType() != type || portion.getState() != state) {
                i++;
                continue;
            }
            if (portion.getAmount() <= remaining) {
                remaining -= portion.getAmount();
                grain.remove(i);
            } else {
                grain.set(i, new Grain(type, portion.getAmount() - remaining, state));
                remaining = 0;
            }
        }
        return new Grain(type, amount, state);
    }

    @Override
    public final boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Storage storage = (Storage) other;
        return name.equals(storage.name) && Double.compare(capacity, storage.capacity) == 0
                && grain.equals(storage.grain);
    }

    @Override
    public final int hashCode() { return Objects.hash(getClass(), name, capacity, grain); }

    @Override
    public String toString() {
        return String.format("%s «%s: %.2f / %.2f кг, %s»",
                getStorageType(), name, getAmount(), capacity, grain);
    }
}
