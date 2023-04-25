package vlad.lailo.markup.exceptions;

import java.io.IOException;

public class DataImageNotFoundException extends IOException {

    public DataImageNotFoundException() {
        super("Data image not found");
    }

    public DataImageNotFoundException(String message) {
        super("Data image not found with name: " + message);
    }
}
