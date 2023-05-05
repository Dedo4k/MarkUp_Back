package vlad.lailo.markup.exceptions;

public class InvalidRoleOperationsException extends RuntimeException {

    public InvalidRoleOperationsException(String message) {
        super("Operations can not be empty for role with id: " + message);
    }
}
