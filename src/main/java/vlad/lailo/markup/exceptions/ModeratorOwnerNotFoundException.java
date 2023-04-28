package vlad.lailo.markup.exceptions;

public class ModeratorOwnerNotFoundException extends RuntimeException {

    public ModeratorOwnerNotFoundException(long id) {
        super("Moderator owner not found for moderator with id: " + id);
    }
}
