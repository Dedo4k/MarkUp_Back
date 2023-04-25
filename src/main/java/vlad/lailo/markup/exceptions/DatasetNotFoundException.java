package vlad.lailo.markup.exceptions;

public class DatasetNotFoundException extends RuntimeException {

    public DatasetNotFoundException(String message) {
        super("Dataset not found with name: " + message);
    }
}
