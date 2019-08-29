package kipoderax.virtuallotto.game.service.user_numbers;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.repositories.HistoryGameRepository;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.commons.dtos.mapper.ApiNumbersMapper;
import kipoderax.virtuallotto.commons.dtos.mapper.BetNumbersMapper;
import kipoderax.virtuallotto.commons.dtos.models.LottoNumbersDto;
import kipoderax.virtuallotto.commons.forms.ResultForm;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.ApiNumberRepository;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.repository.UserBetsRepository;
import kipoderax.virtuallotto.game.repository.UserExperienceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@RunWith(SpringRunner.class)
class UserNumbersServiceTest {

    @Mock
    private BetNumbersMapper betNumbersMapper;
    @Mock
    private ApiNumbersMapper apiNumbersMapper;

    private @Mock UserBetsRepository userBetsRepository;
    private @Mock GameRepository gameRepository;
    private @Mock UserExperienceRepository userExperienceRepository;
    private @Mock UserRepository userRepository;
    private @Mock ApiNumberRepository apiNumberRepository;
    private @Mock HistoryGameRepository historyGameRepository;

    private @Mock WinnerBetsServiceImpl winnerBetsService;

    private @Mock UserSession userSession;

    @InjectMocks
    private UserNumbersService userNumbersService;

    private ResultForm resultForm;
    private int userId;
    private String username;
    @BeforeEach
    void setUp() {
        resultForm = new ResultForm();
        userId = 15;
        username = "player1";
    }

    @Test
    public void addUserExperience() {
        //given
        GameModel gameModel = new GameModel();
        User user = new User();
        user.setUsername(username);

        int[] goalNumbers = {15, 0, 0, 0, 0, 0, 0};
        int currentUserExperience = 100;

        given(userSession.getUser()).willReturn(user);
        given(userSession.isUserLogin()).willReturn(true);
        given(userSession.getUsername()).willReturn(user.getUsername());
        given(userExperienceRepository.findExpByLogin(
                userSession.getUser().getUsername()))
                .willReturn(currentUserExperience);

        //when
        userNumbersService.addUserExperience(gameModel, goalNumbers, resultForm);
        int newUserExperience = currentUserExperience + resultForm.getTotalExp();

        //then
        InOrder inOrder = inOrder(userExperienceRepository);
        inOrder.verify(userExperienceRepository).findExpByLogin(username);
        inOrder.verify(userExperienceRepository).updateExperienceById(
                userSession.getUser().getId(), newUserExperience);
        inOrder.verify(userExperienceRepository).updateLevelById(
                userSession.getUser().getId(), 12);

        assertThat(resultForm.getTotalExp()).isEqualTo(0);
        assertThat(newUserExperience).isEqualTo(100);

        //given with goal one numbers 15 times
        goalNumbers = new int[]{15, 15, 0, 0, 0, 0, 0};

        //when
        userNumbersService.addUserExperience(gameModel, goalNumbers, resultForm);
        newUserExperience = currentUserExperience + resultForm.getTotalExp();

        //then
        assertThat(resultForm.getTotalExp()).isEqualTo(15);
        assertThat(newUserExperience).isEqualTo(115);

        //given with goal two numbers 15 times
        goalNumbers = new int[]{15, 0, 15, 0, 0, 0, 0};

        //when
        userNumbersService.addUserExperience(gameModel, goalNumbers, resultForm);
        newUserExperience = currentUserExperience + resultForm.getTotalExp();

        //then
        assertThat(resultForm.getTotalExp()).isEqualTo(45);
        assertThat(newUserExperience).isEqualTo(145);

        //given with goal three numbers 5 times
        goalNumbers = new int[]{0, 0, 0, 5, 0, 0, 0};

        //when
        userNumbersService.addUserExperience(gameModel, goalNumbers, resultForm);
        newUserExperience = currentUserExperience + resultForm.getTotalExp();

        //then
        assertThat(resultForm.getTotalExp()).isEqualTo(105);
        assertThat(newUserExperience).isEqualTo(205);

        //given with goal four numbers 2 times
        goalNumbers = new int[]{0, 0, 0, 0, 2, 0, 0};

        //when
        userNumbersService.addUserExperience(gameModel, goalNumbers, resultForm);
        newUserExperience = currentUserExperience + resultForm.getTotalExp();

        //then
        assertThat(resultForm.getTotalExp()).isEqualTo(372);
        assertThat(newUserExperience).isEqualTo(472);

        //given with goal five numbers 2 times
        goalNumbers = new int[]{0, 0, 0, 0, 0, 2, 0};

        //when
        userNumbersService.addUserExperience(gameModel, goalNumbers, resultForm);
        newUserExperience = currentUserExperience + resultForm.getTotalExp();

        //then
        assertThat(resultForm.getTotalExp()).isEqualTo(3970);
        assertThat(newUserExperience).isEqualTo(4070);

        //given with goal six numbers 2 times
        goalNumbers = new int[]{0, 0, 0, 0, 0, 0, 2};

        //when
        userNumbersService.addUserExperience(gameModel, goalNumbers, resultForm);
        newUserExperience = currentUserExperience + resultForm.getTotalExp();

        //then
        assertThat(resultForm.getTotalExp()).isEqualTo(30268);
        assertThat(newUserExperience).isEqualTo(30368);
    }

    @Test
    public void costBets() {
        //given
        ResultForm resultForm = new ResultForm();
        GameModel gameModel = new GameModel();

        //when
        userNumbersService.costBets(10, gameModel, resultForm);

        //then
        assertThat(resultForm.getTotalCostBets()).isEqualTo(-30);
    }

    @Test
    public void earnFromGoalWith3NumbersShouldReturnGoalTimesMoneyRewards() {
        //given with one goal three numbers
        int[] goalNumbers = {15, 13, 5, 1, 0, 0, 0};

        //when
        userNumbersService.earnFromGoalNumbers(goalNumbers, resultForm);

        //then
        assertThat(resultForm.getTotalEarn()).isEqualTo(24);

        //given with five goal three numbers
        goalNumbers = new int[]{15, 13, 5, 5, 0, 0, 0};

        //when
        userNumbersService.earnFromGoalNumbers(goalNumbers, resultForm);

        //then
        assertThat(resultForm.getTotalEarn()).isEqualTo(120);
    }

    @Test
    public void earnFromGoalWith4NumbersShouldReturnGoalTimesMoneyRewards() {
        //To check price with goal four numbers go to
        //https://app.lotto.pl/wygrane/?type=dl

        //given with one goal four numbers
        int[] goalNumbers = {15, 13, 5, 0, 1, 0, 0};

        //when
        userNumbersService.earnFromGoalNumbers(goalNumbers, resultForm);

        //then
        assertThat(resultForm.getTotalEarn()).isEqualTo(274);

        //given with five goal four numbers
        goalNumbers = new int[]{15, 13, 5, 0, 5, 0, 0};

        //when
        userNumbersService.earnFromGoalNumbers(goalNumbers, resultForm);

        //then
        assertThat(resultForm.getTotalEarn()).isEqualTo(1370);
    }

    @Test
    public void earnFromGoalWith5NumbersShouldReturnGoalTimesMoneyRewards() {
        //To check price with goal five numbers go to
        //https://app.lotto.pl/wygrane/?type=dl

        //given with 1 goal
        int[] goalNumbers = {15, 13, 5, 0, 0, 1, 0};

        //when
        userNumbersService.earnFromGoalNumbers(goalNumbers, resultForm);

        //then
        assertThat(resultForm.getTotalEarn()).isEqualTo(4782);

        //given with 5 goal
        goalNumbers = new int[]{15, 13, 5, 0, 0, 5, 0};

        //when
        userNumbersService.earnFromGoalNumbers(goalNumbers, resultForm);

        //then
        assertThat(resultForm.getTotalEarn()).isEqualTo(23910);
    }

    @Test
    public void earnFromGoalWith6NumbersShouldReturnGoalTimesMoneyRewards() {
        //To check price with goal six numbers go to
        //https://app.lotto.pl/wygrane/?type=dl

        //given with 1 goal
        int[] goalNumbers = {15, 13, 5, 0, 0, 0, 1};

        //when
        userNumbersService.earnFromGoalNumbers(goalNumbers, resultForm);

        //then
        assertThat(resultForm.getTotalEarn()).isEqualTo(2_000_000);

        //given with 5 goal
        goalNumbers = new int[]{15, 13, 5, 0, 0, 0, 5};

        //when
        userNumbersService.earnFromGoalNumbers(goalNumbers, resultForm);

        //then
        assertThat(resultForm.getTotalEarn()).isEqualTo(10_000_000);
    }

    @Test
    public void resultEarnShouldReturnNegativeEarn() {
        //given
        userNumbersService.resultEarn(-90, 24, resultForm);

        //when
        int totalEarn = resultForm.getFinishResult();

        //then
        assertThat(totalEarn).isEqualTo(-66);
    }

    @Test
    public void resultEarnShouldReturnPositiveEarn() {
        //given
        userNumbersService.resultEarn(-30, 48, resultForm);

        //when
        int totalEarn = resultForm.getFinishResult();

        //then
        assertThat(totalEarn).isEqualTo(18);
    }

    @Test
    public void resultEarnShouldReturnZero() {
        //given
        userNumbersService.resultEarn(-48, 48, resultForm);

        //when
        int totalEarn = resultForm.getFinishResult();

        //then
        assertThat(totalEarn).isEqualTo(0);
    }

    @Test
    public void isNewNumberApiWithDifferentNumbersShouldReturnFalse() {
        //given
        List<Integer> lastApiNumbers = new ArrayList<>();
        List<Integer> currentApiNumbers = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            lastApiNumbers.add(i + 1);
            currentApiNumbers.add(i + 3);
        }

        //when
        boolean differentList = userNumbersService.isNewNumberApi(
                lastApiNumbers, currentApiNumbers);

        //then
        assertThat(differentList).isFalse();
    }

    @Test
    public void isNewNumberApiWithTheSameNumbersShouldReturnTrue() {
        //given
        List<Integer> lastApiNumbers = new ArrayList<>();
        List<Integer> currentApiNumbers = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            lastApiNumbers.add(i + 1);
            currentApiNumbers.add(i + 1);
        }

        //when
        boolean differentList = userNumbersService.isNewNumberApi(
                lastApiNumbers, currentApiNumbers);

        //then
        assertThat(differentList).isTrue();
    }

    @Test
    public void maxBetsForSendWithLevelGreaterThan5ShouldReturnSaldoBy3() {
        //given
        given(userExperienceRepository.findLevelByLogin(username)).willReturn(10);
        given(userRepository.findSaldoByUserId(userId)).willReturn(45);

        //when
        int maxBetsSend = userNumbersService.maxBetsForSend(userId, username);

        //then
        InOrder inOrder = inOrder(userExperienceRepository, userRepository, gameRepository);
        inOrder.verify(userExperienceRepository).findLevelByLogin(username);
        inOrder.verify(userRepository).findSaldoByUserId(userId);
        inOrder.verify(gameRepository).updateMaxBetsToSend(maxBetsSend, userId);

        assertThat(maxBetsSend).isEqualTo(15);
    }

    @Test
    public void maxBetsForSendWithSaldoBy3GreaterThanLevelBy2ShouldReturnLevelBy2() {
        //given
        given(userExperienceRepository.findLevelByLogin(username)).willReturn(45);
        given(userRepository.findSaldoByUserId(userId)).willReturn(300); // 100 > 90

        //when
        int maxBetsSend = userNumbersService.maxBetsForSend(userId, username);

        //then
        InOrder inOrder = inOrder(userExperienceRepository, userRepository, gameRepository);
        inOrder.verify(userExperienceRepository).findLevelByLogin(username);
        inOrder.verify(userRepository).findSaldoByUserId(userId);
        inOrder.verify(gameRepository).updateMaxBetsToSend(maxBetsSend, userId);

        assertThat(maxBetsSend).isEqualTo(90);
    }

    @Test
    public void maxBetsForSendWithLevelLessThan6ShouldReturn10() {
        //given
        given(userExperienceRepository.findLevelByLogin(username)).willReturn(4);
        given(userRepository.findSaldoByUserId(userId)).willReturn(30);

        //when
        int maxBetsSend = userNumbersService.maxBetsForSend(userId, username);

        //then
        InOrder inOrder = inOrder(userExperienceRepository, userRepository, gameRepository);
        inOrder.verify(userExperienceRepository).findLevelByLogin(username);
        inOrder.verify(userRepository).findSaldoByUserId(userId);
        inOrder.verify(gameRepository).updateMaxBetsToSend(maxBetsSend, userId);

        assertThat(maxBetsSend).isEqualTo(10);
    }

    @Test
    public void leftBetsToSend() {
        //given
        given(gameRepository.findMaxBetsToSend(userId)).willReturn(40);
        given(userBetsRepository.AmountBetsByUserId(userId)).willReturn(25);

        //when
        int leftBets = userNumbersService.leftBetsToSend(userId);

        //then
        InOrder inOrder = inOrder(gameRepository, userBetsRepository);
        inOrder.verify(gameRepository).findMaxBetsToSend(userId);
        inOrder.verify(userBetsRepository).AmountBetsByUserId(userId);
        assertThat(leftBets).isEqualTo(15);
    }

    @Test
    public void LeftBetsToSendedBetsIsZeroShouldReturnMaxBetsToSend() {
        //given
        given(gameRepository.findMaxBetsToSend(userId)).willReturn(40);
        given(userBetsRepository.AmountBetsByUserId(userId)).willReturn(0);

        //when
        int leftBets = userNumbersService.leftBetsToSend(userId);

        //then
        InOrder inOrder = inOrder(gameRepository, userBetsRepository);
        inOrder.verify(gameRepository).findMaxBetsToSend(userId);
        inOrder.verify(userBetsRepository).AmountBetsByUserId(userId);
        assertThat(leftBets).isEqualTo(40);
    }

    @Test
    public void LeftBetsToSendedBetsIsMaxBetsSendShouldReturnZero() {
        //given
        given(gameRepository.findMaxBetsToSend(userId)).willReturn(40);
        given(userBetsRepository.AmountBetsByUserId(userId)).willReturn(40);

        //when
        int leftBets = userNumbersService.leftBetsToSend(userId);

        //then
        InOrder inOrder = inOrder(gameRepository, userBetsRepository);
        inOrder.verify(gameRepository).findMaxBetsToSend(userId);
        inOrder.verify(userBetsRepository).AmountBetsByUserId(userId);
        assertThat(leftBets).isEqualTo(0);
    }

    @Test
    public void saveUserInputNumbers() {
        //given
        int[] numbers = new int[]{11, 10, 13, 21, 24, 33};

        //when
        userNumbersService.saveUserInputNumbers(numbers, userId);

        //then
        InOrder inOrder = inOrder(userRepository, userBetsRepository);
        inOrder.verify(userRepository).findSaldoByUserId(userId);
        inOrder.verify(userBetsRepository).saveInputNumbersByIdUser(userId,
                numbers[0], numbers[1], numbers[2], numbers[3], numbers[4], numbers[5]);

        assertThat(numbers[0]).isEqualTo(10);
    }

    @Test
    public void goalBetsWithSuccess() {
        //given
        int success = 3;
        List<Integer> userBets = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            userBets.add(i + 3);
        }
        LottoNumbersDto lottoNumbersDto =
                new LottoNumbersDto(userBets.get(0), userBets.get(1), userBets.get(2),
                        userBets.get(3), userBets.get(4), userBets.get(5));

        //when
        userNumbersService.goalBetsWithSuccess(success, userBets);

        //then
        verify(winnerBetsService).addWinnerBetsWith3Numbers(lottoNumbersDto);

        //given
        success = 4;

        //when
        userNumbersService.goalBetsWithSuccess(success, userBets);

        //then
        verify(winnerBetsService).addWinnerBetsWith4Numbers(lottoNumbersDto);

        //given
        success = 5;

        //when
        userNumbersService.goalBetsWithSuccess(success, userBets);

        //then
        verify(winnerBetsService).addWinnerBetsWith5Numbers(lottoNumbersDto);

        //given
        success = 6;

        //when
        userNumbersService.goalBetsWithSuccess(success, userBets);

        //then
        verify(winnerBetsService).addWinnerBetsWith6Numbers(lottoNumbersDto);
    }

    @Test
    public void generateNumber() {
        //given
        LottoNumbersDto lottoNumbersDto = new LottoNumbersDto();

        //when
        userNumbersService.generateNumber(lottoNumbersDto);

        //then
        assertThat(lottoNumbersDto).isNotNull();
    }
}