package Commands;


public class IncorrectCommandException extends Exception {
    public IncorrectCommandException() {
        super();
    }

    public IncorrectCommandException(String message) {
        super(message);
    }
}
