package vlad.lailo.markup.exceptions;

public class RoleDeleteException extends RuntimeException {

    public RoleDeleteException(String message) {
        super("You can not delete role with id: " + message + " if one or more users have it");
    }
}
