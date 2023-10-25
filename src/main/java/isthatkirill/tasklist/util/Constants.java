package isthatkirill.tasklist.util;

import java.time.format.DateTimeFormatter;

/**
 * @author Kirill Emelyanov
 */

public class Constants {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

}
