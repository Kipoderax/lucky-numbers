package kipoderax.virtuallotto.test.externalProps;

import kipoderax.testConfigExternalProps.ExternalPropsMultiFile;
import kipoderax.virtuallotto.test.jms.FakeJmsBroker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static  org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ExternalPropsMultiFile.class)
public class PropertySourceMultiFileTest {

    @Autowired
    FakeJmsBroker fakeJmsBroker;

    @Test
    public void testPropsSet() throws Exception {
        assertEquals("10.11.12.123", fakeJmsBroker.getUrl());
        assertEquals(4321, fakeJmsBroker.getPort().intValue());
        assertEquals("Kipo", fakeJmsBroker.getUser());
        assertEquals("&%$)(*&^!@!@#$", fakeJmsBroker.getPassword());
    }
}
