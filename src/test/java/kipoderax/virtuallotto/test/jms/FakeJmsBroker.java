package kipoderax.virtuallotto.test.jms;

import lombok.Data;

@Data
public class FakeJmsBroker {

    private String url;
    private Integer port;
    private String user;
    private String password;

}
