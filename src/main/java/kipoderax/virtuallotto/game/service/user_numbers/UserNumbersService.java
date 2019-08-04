package kipoderax.virtuallotto.game.service.user_numbers;

import kipoderax.virtuallotto.auth.entity.HistoryGame;
import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.commons.forms.HistoryGameForm;
import kipoderax.virtuallotto.commons.forms.ResultForm;
import kipoderax.virtuallotto.auth.repositories.HistoryGameRepository;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.commons.dtos.mapper.ApiNumberMapper;
import kipoderax.virtuallotto.commons.dtos.mapper.UserNumbersMapper;
import kipoderax.virtuallotto.commons.dtos.models.ApiNumberDto;
import kipoderax.virtuallotto.commons.dtos.models.UserNumbersDto;
import kipoderax.virtuallotto.commons.validation.InputNumberValidation;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.model.WinnerBets;
import kipoderax.virtuallotto.game.repository.ApiNumberRepository;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.repository.UserBetsRepository;
import kipoderax.virtuallotto.game.repository.UserExperienceRepository;
import kipoderax.virtuallotto.game.service.ConvertToJson;
import kipoderax.virtuallotto.game.service.Experience;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserNumbersService {

    private UserNumbersMapper userNumbersMapper;
    private ApiNumberMapper apiNumberMapper;

    private UserBetsRepository userBetsRepository;
    private GameRepository gameRepository;
    private UserExperienceRepository userExperienceRepository;
    private UserRepository userRepository;
    private ApiNumberRepository apiNumberRepository;
    private HistoryGameRepository historyGameRepository;

    private WinnerBetsServiceImpl winnerBetsService;

    private UserSession userSession;

    public UserNumbersService(UserNumbersMapper userNumbersMapper,
                              ApiNumberMapper apiNumberMapper,

                              UserBetsRepository userBetsRepository,
                              GameRepository gameRepository,
                              UserExperienceRepository userExperienceRepository,
                              UserRepository userRepository,
                              ApiNumberRepository apiNumberRepository,
                              HistoryGameRepository historyGameRepository,

                              WinnerBetsServiceImpl winnerBetsService,

                              UserSession userSession) {

        this.userNumbersMapper = userNumbersMapper;
        this.apiNumberMapper = apiNumberMapper;

        this.userBetsRepository = userBetsRepository;
        this.gameRepository = gameRepository;
        this.userExperienceRepository = userExperienceRepository;
        this.userRepository = userRepository;
        this.apiNumberRepository = apiNumberRepository;
        this.historyGameRepository = historyGameRepository;

        this.winnerBetsService = winnerBetsService;

        this.userSession = userSession;
    }


    public void userNumbersDtos(List<UserNumbersDto> userNumbersDtos, int userId) {

        userBetsRepository.findAllById(userId).stream()
                .map(n -> userNumbersDtos.add(userNumbersMapper.map(n)))
                .collect(Collectors.toList());

    }

    public List<UserNumbersDto> getAllUserNumbersById(int userId) {
        List<UserNumbersDto> userNumbersDtos = new ArrayList<>();

        userNumbersDtos(userNumbersDtos, userId);

        return userNumbersDtos;
    }


    private void userApiNumbers(List<ApiNumberDto> apiNumberDtos, int userId) {

        apiNumberRepository.findAllByUserId(userId).stream()
                .map(n -> apiNumberDtos.add(apiNumberMapper.map(n)))
                .collect(Collectors.toList());

    }

    public List<Integer> getUserApiNumber(int userId) {
        List<ApiNumberDto> apiNumberDtos = new ArrayList<>();

        userApiNumbers(apiNumberDtos, userId);

        List<Integer> intApiDtos = new ArrayList<>();
        intApiDtos.add(apiNumberDtos.get(0).getNumber1());
        intApiDtos.add(apiNumberDtos.get(0).getNumber2());
        intApiDtos.add(apiNumberDtos.get(0).getNumber3());
        intApiDtos.add(apiNumberDtos.get(0).getNumber4());
        intApiDtos.add(apiNumberDtos.get(0).getNumber5());
        intApiDtos.add(apiNumberDtos.get(0).getNumber6());

        return intApiDtos;
    }

    public ResultForm checkUserNumbers(GameModel gameModel, int userId, String username) {
        winnerBetsService.getWinnerBetsWith3Numbers().clear();
        winnerBetsService.getWinnerBetsWith4Numbers().clear();
        winnerBetsService.getWinnerBetsWith5Numbers().clear();
        winnerBetsService.getWinnerBetsWith6Numbers().clear();

        ResultForm resultForm = new ResultForm();
        HistoryGame historyGame = new HistoryGame();
        User user = userSession.getUser();

        List<UserNumbersDto> userNumbersDtos = new ArrayList<>();
        userNumbersDtos(userNumbersDtos, userId);
        Integer maxBetsId = userBetsRepository.AmountBetsByUserId(userId);

        int currentUserNumberGame;

        if (historyGameRepository.amountBets(username) != null) {
            currentUserNumberGame = historyGameRepository.amountBets(username);
        } else { currentUserNumberGame = 0; }

        int newUserNumberGame = currentUserNumberGame + maxBetsId;
        gameRepository.updateNumberGame(newUserNumberGame, userId);

        int[] goalNumbers = {resultForm.getFailGoal(), resultForm.getGoalOneNumber(), resultForm.getGoal2Numbers(),
                resultForm.getGoal3Numbers(), resultForm.getGoal4Numbers(), resultForm.getGoal5Numbers(),
                resultForm.getGoal6Numbers()};

        for (int i = 0; i < maxBetsId; i++) {
            if (maxBetsId == 0) {
                break;
            } else {
                int success = 0;
                List<Integer> currentNumbers = new ArrayList<>();

                for (int value : gameModel.getLastNumbers().subList(0, 6)) {
                    currentNumbers.add(userNumbersDtos.get(i).getNumber1());
                    currentNumbers.add(userNumbersDtos.get(i).getNumber2());
                    currentNumbers.add(userNumbersDtos.get(i).getNumber3());
                    currentNumbers.add(userNumbersDtos.get(i).getNumber4());
                    currentNumbers.add(userNumbersDtos.get(i).getNumber5());
                    currentNumbers.add(userNumbersDtos.get(i).getNumber6());

                    for (int j = 0; j <= 5; j++) {
                        if (value == currentNumbers.get(j)) {
                            gameModel.getAddGoalNumbers().add(gameModel.getLastNumbers().get(j));
                            success++;
                        }
                    }

                }
                goalBetsWithSuccess(success, currentNumbers);
                upgradeAmountFrom3To6(success, goalNumbers, resultForm);
            }
        }

        saveAmountGoalAfterViewResult(resultForm);
        addUserExperience(gameModel, goalNumbers, resultForm);
        costBets(maxBetsId, gameModel, resultForm);
        earnFromGoalNumbers(goalNumbers, resultForm);
        resultEarn(maxBetsId * gameModel.getRewardsMoney()[0], resultForm.getTotalEarn(), resultForm);
        renewUserSaldo(username, userId, resultForm.getTotalEarn(), maxBetsId);
        saveToHistoryUser(gameModel);

        historyGame.setDateGame(saveToHistoryUser(gameModel).substring(0, 10));
        historyGame.setAmountBets(maxBetsId);
        historyGame.setAmountGoalThrees(goalNumbers[3]);
        historyGame.setAmountGoalFours(goalNumbers[4]);
        historyGame.setAmountGoalFives(goalNumbers[5]);
        historyGame.setAmountGoalSixes(goalNumbers[6]);
        historyGame.setExperience(resultForm.getTotalExp());
        historyGame.setResult(resultForm.getFinishResult());
        historyGame.setUser(user);
        historyGameRepository.save(historyGame);
        maxBetsForSend(userId, username);

        return resultForm;
    }

    public String saveToHistoryUser(GameModel gameModel) {
        HistoryGameForm historyGameForm = new HistoryGameForm();
        historyGameForm.setDateGame(gameModel.getDateGame().get(0));

        return historyGameForm.getDateGame();
    }

    public void renewUserSaldo(String username, int userId, int totalEarn, int betsSended) {
        int renewSaldo;
        int currentUserSaldo = userRepository.findSaldoByUserId(userId);
        int maxSaldoForUser = 30 + (userExperienceRepository.findLevelByLogin(username) * 2);
        int maxBetsToSend = gameRepository.findMaxBetsToSend(userId);

        if (maxBetsToSend - betsSended == 0) {

            renewSaldo = currentUserSaldo + maxSaldoForUser + totalEarn;
        } else if (currentUserSaldo < maxSaldoForUser) {

            renewSaldo = maxSaldoForUser + totalEarn;
        } else if (maxBetsToSend - betsSended == maxBetsToSend) {

            renewSaldo = currentUserSaldo;
        }
        else {

            renewSaldo = currentUserSaldo + (betsSended * 3) + totalEarn;
        }

        userRepository.updateUserSaldoByLogin(renewSaldo, userId);
    }

    public void saveAmountGoalAfterViewResult(ResultForm resultForm) {
        int currentAmountOfThree = gameRepository.findCountOfThreeByLogin(userSession.getUser().getUsername());
        int currentAmountOfFour = gameRepository.findCountOfFourByLogin(userSession.getUser().getUsername());
        int currentAmountOfFive = gameRepository.findCountOfFiveByLogin(userSession.getUser().getUsername());
        int currentAmountOfSix = gameRepository.findCountOfSixByLogin(userSession.getUser().getUsername());

        int newAmountOfThree = currentAmountOfThree + resultForm.getGoal3Numbers();
        int newAmountOfFour = currentAmountOfFour + resultForm.getGoal4Numbers();
        int newAmountOfFive = currentAmountOfFive + resultForm.getGoal5Numbers();
        int newAmountOfSix = currentAmountOfSix + resultForm.getGoal6Numbers();

        gameRepository.updateAmountOfThree(newAmountOfThree, userSession.getUser().getId());
        gameRepository.updateAmountOfFour(newAmountOfFour, userSession.getUser().getId());
        gameRepository.updateAmountOfFive(newAmountOfFive, userSession.getUser().getId());
        gameRepository.updateAmountOfSix(newAmountOfSix, userSession.getUser().getId());
    }

    public void upgradeAmountFrom3To6(int success, int[] goalNumbers, ResultForm resultForm) {

        for (int i = 0; i <= 6; i++) {
            if (success == i) {
                goalNumbers[i]++;
            }
        }

        resultForm.setFailGoal(goalNumbers[0]);
        resultForm.setGoalOneNumber(goalNumbers[1]);
        resultForm.setGoal2Numbers(goalNumbers[2]);
        resultForm.setGoal3Numbers(goalNumbers[3]);
        resultForm.setGoal4Numbers(goalNumbers[4]);
        resultForm.setGoal5Numbers(goalNumbers[5]);
        resultForm.setGoal6Numbers(goalNumbers[6]);

    }

    public void addUserExperience(GameModel gameModel, int[] goalNumbers, ResultForm resultForm) {

        Experience experience = new Experience();
        int currentUserExperience = userExperienceRepository.findExpByLogin(userSession.getUser().getUsername());
        int sumExperience = 0;

        for (int i = 1; i <= 6; i++) {
            sumExperience += goalNumbers[i] * gameModel.getRewardsExperience()[i - 1];

        }
        resultForm.setTotalExp(sumExperience);

        int newUserExperience = currentUserExperience + resultForm.getTotalExp();
        userExperienceRepository.updateExperienceById(userSession.getUser().getId(), newUserExperience);
        userExperienceRepository.updateLevelById(userSession.getUser().getId(), experience.currentLevel(newUserExperience));
    }

    public void costBets(int number, GameModel gameModel, ResultForm resultForm) {

        resultForm.setTotalCostBets(number * gameModel.getRewardsMoney()[0]);

    }

    public void earnFromGoalNumbers(int[] goalNumbers, ResultForm resultForm) {

        ConvertToJson convertToJson = new ConvertToJson();

        int sumEarnMoney = 0;
        for (int i = 3; i <= 6; i++) {
            convertToJson.getMoneyRew()[0] = 24;
            sumEarnMoney += goalNumbers[i] * convertToJson.getLastWins(convertToJson.getMoneyRew()[i - 3]);
        }

        resultForm.setTotalEarn(sumEarnMoney);

    }

    public void resultEarn(int totalCost, int winPrice, ResultForm resultForm) {
        resultForm.setFinishResult(winPrice + totalCost);

    }

    public boolean isNewNumberApi(List<Integer> lastApiNumberList, List<Integer> apiNumberList) {

        int success = 0;

        for (int value : lastApiNumberList) {

            for (int i = 0; i < lastApiNumberList.size(); i++) {

                if (value == apiNumberList.get(i)) {

                    success++;
                }
            }
        }

        return success == 6;
    }

    public void maxBetsForSend(int userId, String username) {
        int level = userExperienceRepository.findLevelByLogin(username);
        int userSaldo = userRepository.findSaldoByUserId(userId);
        int leftBets;

        if (level > 5) {
            if (userSaldo / 3 > level * 2) {

                leftBets = level * 2;
            } else {

                leftBets = userSaldo / 3;
            }
        } else {

            leftBets = 10;
        }

        gameRepository.updateMaxBetsToSend(leftBets, userId);
    }

    public int leftBetsToSend(int userId) {

        return gameRepository.findMaxBetsToSend(userId) - userBetsRepository.AmountBetsByUserId(userId);
    }

    public void saveUserInputNumbers(int numbers[], int id) {
        GameModel gameModel = new GameModel();
        InputNumberValidation inputNumberValidation = new InputNumberValidation();
        inputNumberValidation.sort(numbers);

        int currentSaldo = userRepository.findSaldoByUserId(id);
        userBetsRepository.saveInputNumbersByIdUser(id, numbers[0], numbers[1], numbers[2], numbers[3],
                numbers[4], numbers[5]);

        int newSaldo = currentSaldo + gameModel.getRewardsMoney()[0];
        userRepository.updateUserSaldoByLogin(newSaldo, id);
    }

    public void goalBetsWithSuccess(int success, List<Integer> listUserBets) {

        WinnerBets winnerBets = new WinnerBets(listUserBets.get(0), listUserBets.get(1),
                listUserBets.get(2), listUserBets.get(3), listUserBets.get(4), listUserBets.get(5));

        switch (success) {
            case 3:

                winnerBetsService.addWinnerBetsWith3Numbers(winnerBets);
                break;
            case 4:

                winnerBetsService.addWinnerBetsWith4Numbers(winnerBets);
                break;
            case 5:

                winnerBetsService.addWinnerBetsWith5Numbers(winnerBets);
                break;
            case 6:

                winnerBetsService.addWinnerBetsWith6Numbers(winnerBets);
                break;
        }
    }
}