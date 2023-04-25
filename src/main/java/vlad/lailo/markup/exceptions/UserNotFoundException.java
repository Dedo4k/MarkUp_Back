package vlad.lailo.markup.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super("User not found with username: " + message);
    }
}
