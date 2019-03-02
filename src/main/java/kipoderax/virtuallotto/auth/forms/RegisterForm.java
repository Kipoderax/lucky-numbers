package kipoderax.virtuallotto.auth.forms;

import lombok.Data;

@Data
public class RegisterForm {

    private String login;
    private String password;
    private String email;

}