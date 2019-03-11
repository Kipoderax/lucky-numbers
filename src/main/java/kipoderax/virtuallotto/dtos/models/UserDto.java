package kipoderax.virtuallotto.dtos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data @NoArgsConstructor @AllArgsConstructor
@Service
public class UserDto {

    private String login;
    private String password;
    private String email;
    private int saldo;

}