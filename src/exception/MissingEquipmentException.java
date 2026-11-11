package exception;

public class MissingEquipmentException extends Exception {
    public MissingEquipmentException(String message) { super(message); }

    @Override
    public String getMessage() { return "Нет оборудования: " + super.getMessage(); }
}
