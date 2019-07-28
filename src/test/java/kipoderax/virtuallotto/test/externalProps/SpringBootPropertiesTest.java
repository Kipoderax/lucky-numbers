package kipoderax.virtuallotto.test.externalProps;

import kipoderax.virtuallotto.VirtualLottoApplication;
import kipoderax.virtuallotto.test.jms.FakeJmsBroker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = VirtualLottoApplication.class)
@WebAppConfiguration
@TestPropertySource("/application.properties")
public class SpringBootPropertiesTest {
    @Autowired
    FakeJmsBroker fakeJmsBroker;

    @Test
    public void testPropsSet() throws Exception {
        assertEquals("10.11.12.123", fakeJmsBroker.getUrl());
        assertEquals(4321, fakeJmsBroker.getPort().intValue());
        assertEquals("Kipo", fakeJmsBroker.getUser());
        assertEquals("Derax", fakeJmsBroker.getPassword());
    }

}