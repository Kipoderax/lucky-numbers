package kipoderax.virtuallotto.commons.mail;

import java.util.Date;

public interface EmailSender {

    void sendEmail(Email email);
//    void tokenRemaining(Date currentTime, Date tokenTime);
}
