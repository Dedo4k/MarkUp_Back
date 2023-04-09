package vlad.lailo.markup.exceptions;

public class StorageNotFoundException extends RuntimeException {

    public StorageNotFoundException(String message) {
        super("Local storage not found path: " + message);
    }
}
