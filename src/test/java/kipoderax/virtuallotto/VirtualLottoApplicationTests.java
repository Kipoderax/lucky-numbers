package kipoderax.virtuallotto;

import kipoderax.virtuallotto.config.AppConfigForJasyptStarter;
import kipoderax.virtuallotto.config.PropertyServiceForJasyptStarter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VirtualLottoApplicationTests {

    @Test
    public void contextLoads() {
    }

//    @Test
//    public void whenDecryptedPasswordNeeded_GetFromService() {
//        System.setProperty("jasypt.encryptor.password", "password");
//        PropertyServiceForJasyptStarter service = appCtx.
//                getBean(PropertyServiceForJasyptStarter.class);
//
//        assertEquals("Password@1", service.getProperty());
//
//        Environment environment = appCtx.getBean(Environment.class);
//
//        assertEquals(
//                "Password@1",
//                service.getPasswordUsingEnvironment(environment));
//    }

}