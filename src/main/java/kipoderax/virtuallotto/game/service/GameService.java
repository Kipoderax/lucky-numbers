package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.auth.service.UserSession;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.GameRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Data
public class GameService {
    private final SecureRandom randomNumber = new SecureRandom();
    private final GameModel gameModel = new GameModel();
    private final GameRepository gameRepository;

    private final UserSession userSession;

    public GameService(GameRepository gameRepository, UserSession userSession) {
        this.gameRepository = gameRepository;
        this.userSession = userSession;
    }

    public List<Integer> showTarget() {

        return new ArrayList<>(Arrays.asList(gameModel.getTargetEasyVersion()));
    }

    public Set<Integer> generateNumber(GameModel gameModel) {

        while (gameModel.getNumberSet().size() != 6) {

            gameModel.setNumber(randomNumber.nextInt(20) + 1);
            gameModel.getNumberSet().add(gameModel.getNumber());
        }

        return gameModel.getNumberSet();
    }

    public List<Integer> addGoalNumber(GameModel gameModel) {

        int currentSaldo = gameModel.getSaldo();
        int success = 0;

        int currentNumberGame = gameRepository.findNumberGameByLogin(userSession.getUser().getLogin());

        //przejdz po liczbach wygenerowanych
        for (int value : gameModel.getNumberSet()) {

            for (int i = 0; i < gameModel.getNumberSet().size(); i++) {

                //jesli ktoras wartosc liczb losowo wygenerowanych znajduje sie w tablicy
                if (value == gameModel.getTargetEasyVersion()[i]) {

                    //dodaj ja do listy liczb trafionych
                    gameModel.getAddGoalNumbers().add(
                            gameModel.getTargetEasyVersion()[i]);

                    success++;
                }
            }
        }

        //zaktualizuj konto na podstawie ilości powyższych trafień
        upgradeCurrentSaldo(gameModel, success, currentSaldo);
        upgradeNumberGame(currentNumberGame);
        upgradeAmountFRom3To6(success);

        return gameModel.getAddGoalNumbers();
    }

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

    public void upgradeNumberGame(int currentNumberGame) {
        currentNumberGame++;
        gameRepository.updateNumberGame(currentNumberGame, userSession.getUser().getId());
    }

    public void upgradeAmountFRom3To6(int count) {
        int currentAmountOfThree = gameRepository.findCountOfThreeByLogin(userSession.getUser().getLogin());
        int currentAmountOfFour = gameRepository.findCountOfFourByLogin(userSession.getUser().getLogin());
        int currentAmountOfFive = gameRepository.findCountOfFiveByLogin(userSession.getUser().getLogin());
        int currentAmountOfSix = gameRepository.findCountOfSixByLogin(userSession.getUser().getLogin());

        switch (count) {
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

    //todo dokończyć opcje ręcznego wpisywania 6 liczb
    public void inputNumbers(int number) {

        Set<Integer> numbersSet = new TreeSet<>();
        gameModel.setNumber(number);
        numbersSet.add(gameModel.getNumber());

    }

    //todo opcja generowania n zestawow liczb. Dla kazdego zestawu zwrócić obok liczbe trafień, wygraną

    //todo opcja wyboru poziomu trudności gry

    @GetMapping("last-result")
    public String getJSON() {
        HttpURLConnection c = null;
        try {
            URL u = new URL("https://app.lotto.pl/wyniki/?type=dl");
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

}