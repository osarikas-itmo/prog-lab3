package farm;

import enums.ToolType;
import storage.Barn;
import storage.Basket;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class Farm {
    private final List<Field> fields;
    private final List<Basket> baskets;
    private final Barn barn;
    private final Set<ToolType> equipment;

    public Farm(List<Field> fields, List<Basket> baskets, Barn barn, Set<ToolType> equipment) {
        this.fields = List.copyOf(fields);
        this.baskets = List.copyOf(baskets);
        this.barn = Objects.requireNonNull(barn, "Не указан амбар");
        this.equipment = EnumSet.noneOf(ToolType.class);
        this.equipment.addAll(equipment);
    }

    public List<Field> getFields() { return fields; }
    public List<Basket> getBaskets() { return baskets; }
    public Barn getBarn() { return barn; }
    public Set<ToolType> getEquipment() { return Set.copyOf(equipment); }
    public boolean hasEquipment(ToolType tool) { return equipment.contains(tool); }
    public void addEquipment(ToolType tool) { equipment.add(Objects.requireNonNull(tool)); }

    public void advanceTime(int days) {
        if (days < 0) throw new IllegalArgumentException("Число дней не может быть отрицательным");
        fields.forEach(field -> field.advanceTime(days));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Farm farm)) return false;
        return fields.equals(farm.fields) && baskets.equals(farm.baskets)
                && barn.equals(farm.barn) && equipment.equals(farm.equipment);
    }

    @Override
    public int hashCode() { return Objects.hash(fields, baskets, barn, equipment); }

    @Override
    public String toString() {
        return "Ферма: поля=" + fields + ", корзины=" + baskets
                + ", " + barn + ", инструменты=" + equipment;
    }
}
