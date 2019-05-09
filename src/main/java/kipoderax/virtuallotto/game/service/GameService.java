package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.auth.forms.NumbersForm;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.GameRepository;
import kipoderax.virtuallotto.game.repository.UserBetsRepository;
import kipoderax.virtuallotto.game.repository.UserExperienceRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Set;

@Service
@Data
public class GameService {
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

    public List<Integer> showTarget() {

        return gameModel.getTestRealNumbers();
    }
    public List<Integer> showWins() {

        return gameModel.getWins();
    }

    public Set<Integer> generateNumber(GameModel gameModel) {

        while (gameModel.getNumberSet().size() != 6) {

            gameModel.setNumber(randomNumber.nextInt(49) + 1);
            gameModel.getNumberSet().add(gameModel.getNumber());
        }

        return gameModel.getNumberSet();
    }

    public List<Integer> addGoalNumber(GameModel gameModel) {

        int currentSaldo = gameModel.getSaldo();
        int success = 0;

        int currentNumberGame = gameRepository.findNumberGameByLogin(userSession.getUser().getLogin());
        int currentExperience = userExperienceRepository.findExpByLogin(userSession.getUser().getLogin());

        //przejdz po liczbach wygenerowanych
        for (int value : gameModel.getNumberSet()) {

            for (int i = 0; i < gameModel.getNumberSet().size(); i++) {

                //jesli ktoras wartosc liczb losowo wygenerowanych znajduje sie w tablicy
                if (value == gameModel.getTestRealNumbers().get(i)) {

                    //dodaj ja do listy liczb trafionych
                    gameModel.getAddGoalNumbers().add(
                            gameModel.getTestRealNumbers().get(i));

                    success++;
                }
            }
        }

        //zaktualizuj konto na podstawie ilości powyższych trafień
        upgradeCurrentSaldo(gameModel, success, currentSaldo, currentExperience);
        upgradeNumberGame(currentNumberGame);
        upgradeAmountFRom3To6(success);

        return gameModel.getAddGoalNumbers();
    }

    private void upgradeCurrentSaldo(GameModel gameModel, int count, int currentSaldo, int currentExperience) {
        gameModel.setWinPerOneGame(0);

        for (int i = 3; i <= gameModel.getRewardsMoney().length; i++) {

            //jeśli ilość trafień jest co najmniej i
            if (count == i) {

                //powiększ saldo zgodnie z ilością trafień uwzględniając koszt zakładu
                currentSaldo += gameModel.getRewardsMoney()[i - 2] + gameModel.getRewardsMoney()[0];

                //ustaw aktualne saldo
                gameModel.setSaldo(currentSaldo);

                //przypisz odpowiednią wygraną do wyświetlenia
                gameModel.setWinPerOneGame(gameModel.getRewardsMoney()[i-2]);

                //dodaj expa
                currentExperience += gameModel.getRewardsExperience()[i-3];
                gameModel.setExperience(currentExperience);
            }
        }

        if (count < 3) {

            //odejmuj saldo o kosztu zakładu
            currentSaldo += gameModel.getRewardsMoney()[0];
            gameModel.setSaldo(currentSaldo);
        }
    }

    public void upgradeNumberGame(int currentNumberGame) {
        currentNumberGame++;
        gameRepository.updateNumberGame(currentNumberGame, userSession.getUser().getId());
    }

    public void upgradeAmountFRom3To6(int success) {
        int currentAmountOfThree = gameRepository.findCountOfThreeByLogin(userSession.getUser().getLogin());
        int currentAmountOfFour = gameRepository.findCountOfFourByLogin(userSession.getUser().getLogin());
        int currentAmountOfFive = gameRepository.findCountOfFiveByLogin(userSession.getUser().getLogin());
        int currentAmountOfSix = gameRepository.findCountOfSixByLogin(userSession.getUser().getLogin());

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

    public void saveUserInputNumbers(NumbersForm numbersForm, int id) {

        userBetsRepository.saveInputNumbersByIdUser(id, numbersForm.getNumber1(),
                numbersForm.getNumber2(), numbersForm.getNumber3(), numbersForm.getNumber4(),
                numbersForm.getNumber5(), numbersForm.getNumber6());
    }

    //todo opcja generowania n zestawow liczb. Dla kazdego zestawu zwrócić obok liczbe trafień, wygraną

    //todo opcja wyboru poziomu trudności gry

}