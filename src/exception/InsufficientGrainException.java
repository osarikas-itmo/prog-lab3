package exception;

public class InsufficientGrainException extends Exception {
    public InsufficientGrainException(String message) { super(message); }

    @Override
    public String getMessage() { return "Нехватка зерна: " + super.getMessage(); }
}
