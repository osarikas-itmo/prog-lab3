package character;

import exception.MissingEquipmentException;
import farm.Field;
import model.Grain;

public interface Farmer {
    Grain harvest(Field field) throws MissingEquipmentException;
    void sow(Field field, Grain grain);
}
