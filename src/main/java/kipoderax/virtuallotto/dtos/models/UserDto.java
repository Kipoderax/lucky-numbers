package kipoderax.virtuallotto.dtos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class UserDto {

    private String login;
    private String password;
    private String email;
    private int saldo;

}