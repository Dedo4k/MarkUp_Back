package vlad.lailo.markup.exceptions;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String message) {
        super("Role not found with name: " + message);
    }
}
