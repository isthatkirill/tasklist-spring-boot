package isthatkirill.tasklist.mail.service;

import isthatkirill.tasklist.mail.model.MailType;
import isthatkirill.tasklist.user.model.User;

import java.util.Properties;

/**
 * @author Kirill Emelyanov
 */

public interface MailService {

    void sendEmail(User user, MailType type, Properties params);

}
