package kipoderax.virtuallotto.test.config;

import kipoderax.virtuallotto.test.jms.FakeJmsBroker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBootJavaConfig {

    @Value("${kipoderax.jms.server}")
    String jmsServer;

    @Value("${kipoderax.jms.port}")
    Integer jmsPort;

    @Value("${kipoderax.jms.user}")
    String jmsUser;

    @Value("${kipoderax.jms.password}")
    String jmsPassword;

    @Bean
    public FakeJmsBroker fakeJmsBroker() {
        FakeJmsBroker fakeJmsBroker = new FakeJmsBroker();
        fakeJmsBroker.setUrl(jmsServer);
        fakeJmsBroker.setPort(jmsPort);
        fakeJmsBroker.setUser(jmsUser);
        fakeJmsBroker.setPassword(jmsPassword);

        return fakeJmsBroker;
    }
}
