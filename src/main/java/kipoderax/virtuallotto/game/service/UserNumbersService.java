package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.dtos.mapper.UserNumbersMapper;
import kipoderax.virtuallotto.dtos.models.UserNumbersDto;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.UserBetsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserNumbersService {

    private UserNumbersMapper userNumbersMapper;
    private UserBetsRepository userBetsRepository;

    public UserNumbersService(UserNumbersMapper userNumbersMapper,
                              UserBetsRepository userBetsRepository) {
        this.userNumbersMapper = userNumbersMapper;
        this.userBetsRepository = userBetsRepository;
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

    public void checkUserNumbers (int userId, GameModel gameModel) {
        List<UserNumbersDto> userNumbersDtos = new ArrayList<>();

        userNumbersDtos(userNumbersDtos, userId);

        Integer maxBetsId = userBetsRepository.AmountBetsByUserId(userId);

        int[] goalNumbers = {0, 0, 0, 0, 0, 0, 0};

        for (int i = 0; i < maxBetsId; i++) {
            if (maxBetsId == 0) {
                break;
            }
            else {
                int success = 0;
                List<Integer> currentNumbers = new ArrayList<>();
                for (int value : gameModel.getLastNumbers()) {
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
                upgradeAmountFRom3To6(success, goalNumbers);
            }
        }

        System.out.println("Rozegrales " + maxBetsId + " gier");
        for (int i = 0; i <= 6; i++) {
            System.out.println("Trafionych: " + i + ": " + goalNumbers[i]);
        }
        System.out.println("Zdobyles: " + addUserExperience(gameModel, goalNumbers) + " expa");
        System.out.println("Zdobyles: " + addUserSaldo(gameModel, goalNumbers) + " pln");

    }

    public void upgradeAmountFRom3To6(int success, int[] goalNumbers) {

        for (int i = 0; i <= 6; i++) {
            if (success == i) {
                goalNumbers[i] ++;
            }
        }

    }

    public int addUserExperience(GameModel gameModel, int[] goalNumbers) {

        int sumExperience = 0;

        for (int i = 1; i <= 6; i++) {
            sumExperience += goalNumbers[i] * gameModel.getRewardsExperience()[i-1];
        }


        return sumExperience;
    }

    public int addUserSaldo(GameModel gameModel, int[] goalNumbers) {

        int sumSaldo = 0;

        for (int i = 0; i <= 6; i++) {
            if (i > 2) {
                sumSaldo += goalNumbers[i] * gameModel.getRewardsMoney()[i-2];
            } else  {
                sumSaldo += goalNumbers[i] * gameModel.getRewardsMoney()[0];
            }

        }

        return sumSaldo;
    }

}