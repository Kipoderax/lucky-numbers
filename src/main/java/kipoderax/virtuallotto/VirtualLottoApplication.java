package kipoderax.virtuallotto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class VirtualLottoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualLottoApplication.class, args);

    }

}
