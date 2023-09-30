package isthatkirill.tasklist.error.exception.entity;

/**
 * @author Kirill Emelyanov
 */

public class NotUniqueException extends RuntimeException {

    public NotUniqueException(String message) {
        super(message);
    }
}
