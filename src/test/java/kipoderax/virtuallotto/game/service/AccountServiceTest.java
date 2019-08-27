package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.auth.repositories.HistoryGameRepository;
import kipoderax.virtuallotto.commons.forms.HistoryGameForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@RunWith(SpringRunner.class)
class AccountServiceTest {

    @Mock
    private HistoryGameRepository historyGameRepository;

    @InjectMocks
    private AccountService accountService;

    private HistoryGameForm historyGameForm;
    private String username1, username2;
    @BeforeEach
    void setUp() {
        historyGameForm = new HistoryGameForm();
        username1 = "player1";
        username2 = "player2";
    }

    @Test
    void setDefaultValuesNullShouldReturnZero() {
        //given
        given(historyGameRepository.amountBets(username1)).willReturn(null);
        given(historyGameRepository.amountGoalThrees(username1)).willReturn(null);
        given(historyGameRepository.amountGoalFours(username1)).willReturn(null);
        given(historyGameRepository.amountGoalFives(username1)).willReturn(null);
        given(historyGameRepository.amountGoalSixes(username1)).willReturn(null);
        given(historyGameRepository.userExperience(username1)).willReturn(null);

        //when
        accountService.setDefaultValues(historyGameForm, username1);

        //then
        verify(historyGameRepository).amountBets(username1);
        verify(historyGameRepository).amountGoalThrees(username1);
        verify(historyGameRepository).amountGoalFours(username1);
        verify(historyGameRepository).amountGoalFives(username1);
        verify(historyGameRepository).amountGoalSixes(username1);
        verify(historyGameRepository).userExperience(username1);

        assertAll(
                () -> assertThat(historyGameForm.getAmountBets()).isEqualTo(0),
                () -> assertThat(historyGameForm.getAmountGoalThrees()).isEqualTo(0),
                () -> assertThat(historyGameForm.getAmountGoalFours()).isEqualTo(0),
                () -> assertThat(historyGameForm.getAmountGoalFives()).isEqualTo(0),
                () -> assertThat(historyGameForm.getAmountGoalSixes()).isEqualTo(0),
                () -> assertThat(historyGameForm.getExperience()).isEqualTo(0)
        );
    }

    @Test
    void setDefaultValuesNotNull() {
        //given
        given(historyGameRepository.amountBets(username2)).willReturn(312);
        given(historyGameRepository.amountGoalThrees(username2)).willReturn(21);
        given(historyGameRepository.amountGoalFours(username2)).willReturn(7);
        given(historyGameRepository.amountGoalFives(username2)).willReturn(3);
        given(historyGameRepository.amountGoalSixes(username2)).willReturn(1);
        given(historyGameRepository.userExperience(username2)).willReturn(2513);

        //when
        accountService.setDefaultValues(historyGameForm, username2);

        //then
        verify(historyGameRepository, times(2)).amountBets(username2);
        verify(historyGameRepository, times(2)).amountGoalThrees(username2);
        verify(historyGameRepository, times(2)).amountGoalFours(username2);
        verify(historyGameRepository, times(2)).amountGoalFives(username2);
        verify(historyGameRepository, times(2)).amountGoalSixes(username2);
        verify(historyGameRepository, times(2)).userExperience(username2);

        assertAll(
                () -> assertThat(historyGameForm.getAmountBets()).isEqualTo(312),
                () -> assertThat(historyGameForm.getAmountGoalThrees()).isEqualTo(21),
                () -> assertThat(historyGameForm.getAmountGoalFours()).isEqualTo(7),
                () -> assertThat(historyGameForm.getAmountGoalFives()).isEqualTo(3),
                () -> assertThat(historyGameForm.getAmountGoalSixes()).isEqualTo(1),
                () -> assertThat(historyGameForm.getExperience()).isEqualTo(2513)
        );
    }


    @Test
    void experienceNullShouldReturnZero() {
        //given
        given(historyGameRepository.userExperience(username1)).willReturn(null);

        //when
        accountService.experience(historyGameForm, username1);

        //then
        verify(historyGameRepository).userExperience(username1);
        assertThat(historyGameForm.getExperience()).isEqualTo(0);
    }

    @Test
    void experienceisNotNull() {
        //given
        given(historyGameRepository.userExperience(username2)).willReturn(2513);

        //when
        accountService.experience(historyGameForm, username2);

        //then
        verify(historyGameRepository, times(2)).userExperience(username2);
        assertThat(historyGameForm.getExperience()).isEqualTo(2513);
    }

    @Test
    void amountGoalSixesNullShouldReturnZero() {
        //given
        given(historyGameRepository.amountGoalSixes(username1)).willReturn(null);

        //when
        accountService.amountGoalSixes(historyGameForm, username1);

        //then
        verify(historyGameRepository).amountGoalSixes(username1);
        assertThat(historyGameForm.getAmountGoalSixes()).isEqualTo(0);
    }

    @Test
    void amountGoalSixesNotNull() {
        //given
        given(historyGameRepository.amountGoalSixes(username2)).willReturn(1);

        //when
        accountService.amountGoalSixes(historyGameForm, username2);

        //then
        verify(historyGameRepository, times(2)).amountGoalSixes(username2);
        assertThat(historyGameForm.getAmountGoalSixes()).isEqualTo(1);
    }

    @Test
    void amountGoalFivesNullShouldReturnZero() {
        //given
        given(historyGameRepository.amountGoalFives(username1)).willReturn(null);

        //when
        accountService.amountGoalFives(historyGameForm, username1);

        //then
        verify(historyGameRepository).amountGoalFives(username1);
        assertThat(historyGameForm.getAmountGoalFives()).isEqualTo(0);
    }

    @Test
    void amountGoalFivesNotNull() {
        //given
        given(historyGameRepository.amountGoalFives(username2)).willReturn(3);

        //when
        accountService.amountGoalFives(historyGameForm, username2);

        //then
        verify(historyGameRepository, times(2)).amountGoalFives(username2);
        assertThat(historyGameForm.getAmountGoalFives()).isEqualTo(3);
    }

    @Test
    void amountGoalFoursNullShouldReturnZero() {
        //given
        given(historyGameRepository.amountGoalFours(username1)).willReturn(null);

        //when
        accountService.amountGoalFours(historyGameForm, username1);

        //then
        verify(historyGameRepository).amountGoalFours(username1);
        assertThat(historyGameForm.getAmountGoalFours()).isEqualTo(0);
    }

    @Test
    void amountGoalFoursNotNull() {
        //given
        given(historyGameRepository.amountGoalFours(username2)).willReturn(7);

        //when
        accountService.amountGoalFours(historyGameForm, username2);

        //then
        verify(historyGameRepository, times(2)).amountGoalFours(username2);
        assertThat(historyGameForm.getAmountGoalFours()).isEqualTo(7);
    }

    @Test
    void amountGoalThreesNullShouldReturnZero() {
        //given
        given(historyGameRepository.amountGoalThrees(username1)).willReturn(null);

        //when
        accountService.amountGoalThrees(historyGameForm, username1);

        //then
        verify(historyGameRepository).amountGoalThrees(username1);
        assertThat(historyGameForm.getAmountGoalThrees()).isEqualTo(0);
    }

    @Test
    void amountGoalThreesNotNull() {
        //given
        given(historyGameRepository.amountGoalThrees(username2)).willReturn(21);

        //when
        accountService.amountGoalThrees(historyGameForm, username2);

        //then
        verify(historyGameRepository, times(2)).amountGoalThrees(username2);
        assertThat(historyGameForm.getAmountGoalThrees()).isEqualTo(21);
    }

    @Test
    void amountBetsNullShouldReturnZero() {
        //given
        given(historyGameRepository.amountBets(username1)).willReturn(null);

        //when
        accountService.amountBets(historyGameForm, username1);

        //then
        verify(historyGameRepository).amountBets(username1);
        assertThat(historyGameForm.getAmountBets()).isEqualTo(0);
    }

    @Test
    void amountBetsNotNull() {
        //given
        given(historyGameRepository.amountBets(username2)).willReturn(312);

        //when
        accountService.amountBets(historyGameForm, username2);

        //then
        verify(historyGameRepository, times(2)).amountBets(username2);
        assertThat(historyGameForm.getAmountBets()).isEqualTo(312);
    }
}