package vlad.lailo.markup.exceptions;

public class OperationNotFoundException extends RuntimeException {

    public OperationNotFoundException(String message) {
        super("Operation not found with id: " + message);
    }
}
