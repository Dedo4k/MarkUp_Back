package vlad.lailo.markup.exceptions;

import java.io.IOException;

public class DataLayoutNotFoundException extends IOException {

    public DataLayoutNotFoundException() {
        super("Data layout not found");
    }

    public DataLayoutNotFoundException(String message) {
        super("Data layout not found with name: " + message);
    }
}
