package kipoderax.virtuallotto;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VirtualLottoApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void getSaldoFromUserRepository() {
        //given
        int exp = userRepository.findSaldoByUserId(20);

        //then
//        assertThat(exp).isEqualTo(2);
        System.out.println("exp: " + exp);
    }

}