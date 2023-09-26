package isthatkirill.tasklist.error;

/**
 * @author Kirill Emelyanov
 */

public class NotUniqueException extends RuntimeException {

    public NotUniqueException(String message) {
        super(message);
    }
}
