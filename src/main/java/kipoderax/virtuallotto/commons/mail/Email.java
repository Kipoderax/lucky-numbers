package kipoderax.virtuallotto.commons.mail;

import lombok.Data;

@Data
public class Email {

    private String subject;
    private String address;
    private String body;

}
