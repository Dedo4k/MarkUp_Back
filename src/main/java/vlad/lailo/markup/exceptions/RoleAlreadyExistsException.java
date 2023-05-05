package vlad.lailo.markup.exceptions;

public class RoleAlreadyExistsException extends RuntimeException {

    public RoleAlreadyExistsException(String message) {
        super("Role already exists with id: " + message);
    }
}
