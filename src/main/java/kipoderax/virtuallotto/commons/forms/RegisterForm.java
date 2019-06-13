package kipoderax.virtuallotto.commons.forms;

import lombok.Data;

@Data
public class RegisterForm {

    private String username;
    private String login;
    private String password;
    private String confirmPassword;
    private String email;

    private String newPassword;
    private String confirmNewPassword;

}