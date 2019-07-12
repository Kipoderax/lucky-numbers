package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.commons.forms.NumbersForm;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.commons.validation.InputNumberValidation;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.repository.UserBetsRepository;
import kipoderax.virtuallotto.game.repository.UserExperienceRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
@Data
public class GameService {

    //todo tego nie będzie. Ta mała platformówka dla niezarejestrowanych bedzie w js
    //todo ponizszy kod pomigruje do UserNumberService z drobnymi modyfikacjami

    private final SecureRandom randomNumber = new SecureRandom();
    private final GameModel gameModel = new GameModel();
    private final ConvertToJson convertToJson = new ConvertToJson();
    private final GameRepository gameRepository;
    private final UserBetsRepository userBetsRepository;
    private final UserExperienceRepository userExperienceRepository;

    private final UserSession userSession;

    public GameService(GameRepository gameRepository, UserSession userSession,
                       UserBetsRepository userBetsRepository,
                       UserExperienceRepository userExperienceRepository) {

        this.userBetsRepository = userBetsRepository;
        this.gameRepository = gameRepository;
        this.userSession = userSession;
        this.userExperienceRepository = userExperienceRepository;
    }

    public int[] showTarget() {
        InputNumberValidation inputNumberValidation = new InputNumberValidation();
        int numbers[] = new int[6];
        gameModel.getLastNumbers();

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = gameModel.getLastNumbers().get(i);
        }

        inputNumberValidation.sort(numbers);
        return numbers;
    }

    public NumbersForm generateNumber(GameModel gameModel, NumbersForm numbersForm) {

        Set<Integer> numberSet = new TreeSet<>();

        while (numberSet.size() != 6) {

            gameModel.setNumber(randomNumber.nextInt(49) + 1);
            numberSet.add(gameModel.getNumber());
        }

            gameModel.getNumberSet().addAll(numberSet);

        numbersForm.setNumber1(gameModel.getNumberSet().get(0));
        numbersForm.setNumber2(gameModel.getNumberSet().get(1));
        numbersForm.setNumber3(gameModel.getNumberSet().get(2));
        numbersForm.setNumber4(gameModel.getNumberSet().get(3));
        numbersForm.setNumber5(gameModel.getNumberSet().get(4));
        numbersForm.setNumber6(gameModel.getNumberSet().get(5));

        return numbersForm;
    }

    public List<Integer> addGoalNumber(GameModel gameModel) {

        int currentSaldo = gameModel.getSaldo();
        int success = 0;

//        int currentNumberGame = gameRepository.findNumberGameByLogin(userSession.getUser().getUsername());
        int currentExperience = userExperienceRepository.findExpByLogin(userSession.getUser().getUsername());

        //przejdz po liczbach wygenerowanych
        for (int value : gameModel.getNumberSet()) {

            for (int i = 0; i < gameModel.getNumberSet().size(); i++) {

                //jesli ktoras wartosc liczb losowo wygenerowanych znajduje sie w tablicy
                if (value == gameModel.getLastNumbers().get(i)) {

                    //dodaj ja do listy liczb trafionych
                    gameModel.getAddGoalNumbers().add(
                            gameModel.getLastNumbers().get(i));

                    success++;
                }
            }
        }

        //zaktualizuj konto na podstawie ilości powyższych trafień
        upgradeCurrentSaldo(gameModel, success, currentSaldo, currentExperience);
//        upgradeNumberGame(currentNumberGame);
        upgradeAmountFRom3To6(success);

        return gameModel.getAddGoalNumbers();
    }

    private void upgradeCurrentSaldo(GameModel gameModel, int success, int currentSaldo, int currentExperience) {
        gameModel.setWinPerOneGame(0);

        for (int i = 3; i <= gameModel.getRewardsMoney().length; i++) {

            //jeśli ilość trafień jest co najmniej i
            if (success == i) {

                //powiększ saldo zgodnie z ilością trafień uwzględniając koszt zakładu
                currentSaldo += gameModel.getRewardsMoney()[i - 2] + gameModel.getRewardsMoney()[0];

                //ustaw aktualne saldo
                gameModel.setSaldo(currentSaldo);

                //przypisz odpowiednią wygraną do wyświetlenia
                gameModel.setWinPerOneGame(gameModel.getRewardsMoney()[i-2]);
            }
        }
        for (int i = 1; i <= gameModel.getRewardsMoney().length; i++) {

            //jeśli ilość trafień jest co najmniej i
            if (success == i) {

                //dodaj expa
                currentExperience += gameModel.getRewardsExperience()[i-1];
                gameModel.setExperience(currentExperience);
            }
        }

        if (success < 3) {

            //odejmuj saldo o kosztu zakładu
            currentSaldo += gameModel.getRewardsMoney()[0];
            gameModel.setSaldo(currentSaldo);
        }
    }

//    public void upgradeNumberGame(int currentNumberGame) {
//        currentNumberGame++;
//        gameRepository.updateNumberGame(currentNumberGame, userSession.getUser().getId());
//    }

    public void upgradeAmountFRom3To6(int success) {
        int currentAmountOfThree = gameRepository.findCountOfThreeByLogin(userSession.getUser().getUsername());
        int currentAmountOfFour = gameRepository.findCountOfFourByLogin(userSession.getUser().getUsername());
        int currentAmountOfFive = gameRepository.findCountOfFiveByLogin(userSession.getUser().getUsername());
        int currentAmountOfSix = gameRepository.findCountOfSixByLogin(userSession.getUser().getUsername());

        switch (success) {
            case 3:
                currentAmountOfThree++;
                gameRepository.updateAmountOfThree(currentAmountOfThree, userSession.getUser().getId());
                break;
            case 4:
                currentAmountOfFour++;
                gameRepository.updateAmountOfFour(currentAmountOfFour, userSession.getUser().getId());
                break;
            case 5:
                currentAmountOfFive++;
                gameRepository.updateAmountOfFive(currentAmountOfFive, userSession.getUser().getId());
                break;
            case 6:
                currentAmountOfSix++;
                gameRepository.updateAmountOfSix(currentAmountOfSix, userSession.getUser().getId());
                break;
        }
    }

    public int getSaldo(GameModel gameModel) {

        return gameModel.getSaldo();
    }

    public int getMyWin(GameModel gameModel) {

        return gameModel.getWinPerOneGame();
    }
}