package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.repositories.HistoryGameRepository;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SumUpInformation {

    private HistoryGameDtoService historyGameDtoService;
    private UserRepository userRepository;
    private HistoryGameRepository historyGameRepository;

    public SumUpInformation(HistoryGameDtoService historyGameDtoService, UserRepository userRepository, HistoryGameRepository historyGameRepository) {
        this.historyGameDtoService = historyGameDtoService;
        this.userRepository = userRepository;
        this.historyGameRepository = historyGameRepository;
    }

    public Integer getTotalBets(GameModel gameModel, Integer amountBets) {
        for (int i = 1; i <= userRepository.findMaxId(); i++) {
            Optional<User> optionalUser =
                    userRepository.findById(i);

            if (optionalUser.isPresent() && historyGameRepository.amountRecords(i) != 0) {

                if (historyGameDtoService.getAllHistoryGames(i).get(0).getDateGame().equals(gameModel.getDateGame().get(0))) {

                    amountBets += historyGameDtoService.getAllHistoryGames(i).get(0).getAmountBets();
                }
            }
        }

        System.out.println();

        return amountBets;
    }
}
