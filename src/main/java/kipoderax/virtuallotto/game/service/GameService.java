package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.model.GameModel;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
@Data
public class GameService {
    private final SecureRandom randomNumber;

    private final UserRepository userRepository;
    private final UserSession userSession;
    private final GameModel gameModel = new GameModel();

    public GameService(UserRepository userRepository,
                       UserSession userSession) {

        this.randomNumber = new SecureRandom();

        this.userRepository = userRepository;
        this.userSession = userSession;

    }

    //SHOW TARGET
    public List<Integer> showTarget() {

        return new ArrayList<>(Arrays.asList(gameModel.getTargetEasyVersion()));
    }

    //GENERATE NUMBER
    public Set<Integer> generateNumber(GameModel gameModel) {

        while (gameModel.getNumberSet().size() != 6) {

            gameModel.setNumber(randomNumber.nextInt(25) + 1);
            gameModel.getNumberSet().add(gameModel.getNumber());
        }

        return gameModel.getNumberSet();
    }

    //GOAL NUMBER
    public List<Integer> addGoalNumber(GameModel gameModel) {

        int currentSaldo = gameModel.getSaldo();
        int count = 0; //licznik trafien

        //przejdz po liczbach wygenerowanych
        for (int value : gameModel.getNumberSet()) {

            for (int i = 0; i < gameModel.getNumberSet().size(); i++) {

                //jesli ktoras wartosc liczb losowo wygenerowanych znajduje sie w tablicy
                if (value == gameModel.getTargetEasyVersion()[i]) {

                    //dodaj ja do listy liczb trafionych
                    gameModel.getAddGoalNumbers().add(
                            gameModel.getTargetEasyVersion()[i]);

                    count++;
                }
            }
        }

        //zaktualizuj konto na podstawie ilości powyższych trafień
        upgradeCurrentSaldo(gameModel, count, currentSaldo);

        return gameModel.getAddGoalNumbers();
    }

    //UPGRADE SALDO
    private void upgradeCurrentSaldo(GameModel gameModel, int count, int currentSaldo) {
        gameModel.setWinPerOneGame(0);

        for (int i = 3; i <= gameModel.getRewards().length; i++) {

            //jeśli ilość trafień jest co najmniej i
            if (count == i) {

                //powiększ saldo zgodnie z ilością trafień uwzględniając koszt zakładu
                currentSaldo += gameModel.getRewards()[i - 2] + gameModel.getRewards()[0];

                //ustaw aktualne saldo
                gameModel.setSaldo(currentSaldo);

                //przypisz odpowiednią wygraną do wyświetlenia
                gameModel.setWinPerOneGame(gameModel.getRewards()[i-2]);
            }
        }

        if (count < 3) {

            //odejmuj saldo o kosztu zakładu
            currentSaldo += gameModel.getRewards()[0];
            gameModel.setSaldo(currentSaldo);
        }
    }

    //GET SALDO
    public int getSaldo(GameModel gameModel) {

        return gameModel.getSaldo();
    }

    //GET ACTUAL WIN
    public int getMyWin(GameModel gameModel) {

        return gameModel.getWinPerOneGame();
    }

    //INPUT NUMBERS
    //todo dokończyć opcje ręcznego wpisywania 6 liczb
    public void inputNumbers(int number) {

        Set<Integer> numbersSet = new TreeSet<>();
        gameModel.setNumber(number);
        numbersSet.add(gameModel.getNumber());

    }

    //todo opcja generowania n zestawow liczb. Dla kazdego zestawu zwrócić obok liczbe trafień, wygraną

    //todo opcja wyboru poziomu trudności gry
}