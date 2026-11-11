package exception;

public class StorageOverflowException extends Exception {
    public StorageOverflowException(String message) { super(message); }
    @Override
    public String getMessage() { return "Переполнение хранилища: " + super.getMessage(); }
}
