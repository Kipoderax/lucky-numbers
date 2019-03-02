package kipoderax.virtuallotto.auth.service;

import kipoderax.virtuallotto.auth.entity.User;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service
@Data
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession {

    private String login;
    private boolean isUserLogin;
    private User user;

}