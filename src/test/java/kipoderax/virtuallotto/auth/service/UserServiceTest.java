package kipoderax.virtuallotto.auth.service;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.game.repository.ApiNumberRepository;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.repository.UserExperienceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@RunWith(SpringRunner.class)
class UserServiceTest {

    private @Mock UserSession userSession;
    private @Mock UserRepository userRepository;
    private @Mock GameRepository gameRepository;
    private @Mock UserExperienceRepository userExperienceRepository;
    private @Mock ApiNumberRepository apiNumberRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void isUsernameFree() {
        //given
        given(userService.isUsernameFree(anyString())).willReturn(true);

        //when
        boolean usernameIsFree = userService.isUsernameFree(anyString());

        //then
        assertTrue(usernameIsFree);
    }

    @Test
    public void isUsernameNotFree() {
        //given
        given(userService.isUsernameFree(anyString())).willReturn(false);

        //when
        boolean usernameIsFree = userService.isUsernameFree(anyString());

        //then
        assertFalse(usernameIsFree);
    }

    @Test
    public void isEmailFree() {
        //given
        given(userService.isEmailFree(anyString())).willReturn(true);

        //when
        boolean emailIsFree = userService.isEmailFree(anyString());

        //then
        verify(userRepository).existsByEmail(anyString());
        assertTrue(emailIsFree);
    }

    @Test
    public void isEmailNotFree() {
        //given
        given(userService.isEmailFree(anyString())).willReturn(false);

        //when
        boolean emailIsFree = userService.isEmailFree(anyString());

        //then
        verify(userRepository).existsByEmail(anyString());
        assertFalse(emailIsFree);
    }

    @Test
    public void isCorrectCurrentPassword() {

    }
}