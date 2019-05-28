package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.auth.forms.HistoryGameForm;
import kipoderax.virtuallotto.auth.forms.ResultForm;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.commons.dtos.mapper.ApiNumberMapper;
import kipoderax.virtuallotto.commons.dtos.mapper.UserNumbersMapper;
import kipoderax.virtuallotto.commons.dtos.models.ApiNumberDto;
import kipoderax.virtuallotto.commons.dtos.models.UserNumbersDto;
import kipoderax.virtuallotto.commons.validation.InputNumberValidation;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.ApiNumberRepository;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.repository.UserBetsRepository;
import kipoderax.virtuallotto.game.repository.UserExperienceRepository;
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

    private UserSession userSession;
    private ConvertToJson convertToJson;

    public UserNumbersService(UserNumbersMapper userNumbersMapper,
                              ApiNumberMapper apiNumberMapper,

                              UserBetsRepository userBetsRepository,
                              GameRepository gameRepository,
                              UserExperienceRepository userExperienceRepository,
                              UserRepository userRepository,
                              ApiNumberRepository apiNumberRepository,

                              UserSession userSession,
                              ConvertToJson convertToJson) {

        this.userNumbersMapper = userNumbersMapper;
        this.apiNumberMapper = apiNumberMapper;

        this.userBetsRepository = userBetsRepository;
        this.gameRepository = gameRepository;
        this.userExperienceRepository = userExperienceRepository;
        this.userRepository = userRepository;
        this.apiNumberRepository = apiNumberRepository;

        this.userSession = userSession;
        this.convertToJson = convertToJson;
    }


    public List<UserNumbersDto> userNumbersDtos(List<UserNumbersDto> userNumbersDtos, int userId) {

        userBetsRepository.findAllById(userId).stream()
                .map(n -> userNumbersDtos.add(userNumbersMapper.map(n)))
                .collect(Collectors.toList());

        return userNumbersDtos;
    }

    public List<UserNumbersDto> getAllUserNumbersById(int userId) {
        List<UserNumbersDto> userNumbersDtos = new ArrayList<>();

        userNumbersDtos(userNumbersDtos, userId);

        return userNumbersDtos;
    }

    public List<ApiNumberDto> userApiNumbers(List<ApiNumberDto> apiNumberDtos, int userId) {

        apiNumberRepository.findAllByUserId(userId).stream()
                .map(n -> apiNumberDtos.add(apiNumberMapper.map(n)))
                .collect(Collectors.toList());

        return apiNumberDtos;
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

    public ResultForm checkUserNumbers (GameModel gameModel, int userId) {

        ResultForm resultForm = new ResultForm();

        List<UserNumbersDto> userNumbersDtos = new ArrayList<>();
        userNumbersDtos(userNumbersDtos, userId);
        Integer maxBetsId = userBetsRepository.AmountBetsByUserId(userId);
        int currentUserNumberGame = gameRepository.findNumberGameByLogin(userSession.getUser().getId());

        theNumberOfTheGame(maxBetsId);
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
                upgradeAmountFrom3To6(success, goalNumbers, resultForm);
            }
        }

        saveAmountGoalAfterViewResult(resultForm);
        addUserExperience(gameModel, goalNumbers, resultForm);
        costBets(maxBetsId, gameModel, resultForm);
        earnFromGoalNumbers(goalNumbers, gameModel, resultForm);
        resultEarn(maxBetsId * gameModel.getRewardsMoney()[0], resultForm.getTotalEarn(), resultForm);
        renewUserSaldo(userId, resultForm.getTotalEarn());
        saveToHistoryUser(gameModel);

        return resultForm;
    }

    public void saveToHistoryUser(GameModel gameModel) {
        HistoryGameForm historyGameForm = new HistoryGameForm();
        historyGameForm.setDateGame(gameModel.getDateGame());
        System.out.println("saveToHistoryUser: " + gameModel.getDateGame().substring(0, 10));
        System.out.println("saveToHistoryUser2: " + historyGameForm.getDateGame().substring(0, 10));
    }

    public void renewUserSaldo(int userId, int totalEarn) {
        int renewSaldo;
        int currentUserSaldo = userRepository.findSaldoByLogin(userId);
        int maxSaldoForUser = 30 + 2*userExperienceRepository.findLevelByLogin(userId);

        if (currentUserSaldo <= maxSaldoForUser) {

            renewSaldo = maxSaldoForUser + totalEarn;
            userRepository.updateUserSaldoByLogin(renewSaldo, userId);
        } else {

            renewSaldo = currentUserSaldo + totalEarn;
            userRepository.updateUserSaldoByLogin(renewSaldo, userId);
        }
    }

    public void saveAmountGoalAfterViewResult(ResultForm resultForm) {
        int currentAmountOfThree = gameRepository.findCountOfThreeByLogin(userSession.getUser().getId());
        int currentAmountOfFour = gameRepository.findCountOfFourByLogin(userSession.getUser().getId());
        int currentAmountOfFive = gameRepository.findCountOfFiveByLogin(userSession.getUser().getId());
        int currentAmountOfSix = gameRepository.findCountOfSixByLogin(userSession.getUser().getId());

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

    public int addUserExperience(GameModel gameModel, int[] goalNumbers, ResultForm resultForm) {

        Experience experience = new Experience();
        int currentUserExperience = userExperienceRepository.findExpByLogin(userSession.getUser().getId());
        int sumExperience = 0;

        for (int i = 1; i <= 6; i++) {
            sumExperience += goalNumbers[i] * gameModel.getRewardsExperience()[i-1];

        }
        resultForm.setTotalExp(sumExperience);

        int newUserExperience = currentUserExperience + resultForm.getTotalExp();
        userExperienceRepository.updateExperienceById(userSession.getUser().getId(), newUserExperience);
        userExperienceRepository.updateLevelById(userSession.getUser().getId(), experience.reachNextLevel(newUserExperience));
        return resultForm.getTotalExp();
    }

    public int theNumberOfTheGame(int number) {

        return number;
    }

    public int costBets(int number, GameModel gameModel, ResultForm resultForm) {

        resultForm.setTotalCostBets(number * gameModel.getRewardsMoney()[0]);

        return resultForm.getTotalCostBets();
    }

    public int earnFromGoalNumbers (int[] goalNumbers, GameModel gameModel, ResultForm resultForm) {

        int sumEarnMoney = 0;
        for (int i = 3; i <= 6; i++) {
            sumEarnMoney += goalNumbers[i] * gameModel.getRewardsMoney()[i-2];
        }

        resultForm.setTotalEarn(sumEarnMoney);

        return resultForm.getTotalEarn();
    }

    public int resultEarn(int totalCost, int winPrice, ResultForm resultForm) {
        resultForm.setFinishResult(winPrice + totalCost);

        return resultForm.getFinishResult();
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

    public void saveUserInputNumbers(int numbers[], int id) {
        GameModel gameModel = new GameModel();
        InputNumberValidation inputNumberValidation = new InputNumberValidation();
        inputNumberValidation.sort(numbers);

        int currentSaldo = userRepository.findSaldoByLogin(id);
        userBetsRepository.saveInputNumbersByIdUser(id, numbers[0], numbers[1], numbers[2], numbers[3],
                numbers[4], numbers[5]);

        int newSaldo = currentSaldo + gameModel.getRewardsMoney()[0];
        userRepository.updateUserSaldoByLogin(newSaldo, id);
    }
}