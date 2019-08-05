package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.repositories.HistoryGameRepository;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.game.entity.LatestInfoFromAllPlayers;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.LatestInfoFromAllPlayersRepository;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Data
public class SumUpInformation {

    private HistoryGameDtoService historyGameDtoService;
    private UserRepository userRepository;
    private HistoryGameRepository historyGameRepository;
    private LatestInfoFromAllPlayersRepository latestInfoFromAllPlayersRepository;

    public SumUpInformation(HistoryGameDtoService historyGameDtoService,
                            UserRepository userRepository,
                            HistoryGameRepository historyGameRepository,
                            LatestInfoFromAllPlayersRepository latestInfoFromAllPlayersRepository) {

        this.historyGameDtoService = historyGameDtoService;
        this.userRepository = userRepository;
        this.historyGameRepository = historyGameRepository;
        this.latestInfoFromAllPlayersRepository = latestInfoFromAllPlayersRepository;
    }

    @Transactional
    @Scheduled(cron = "00 30 20 * * 2,4,6")
    public void getTotalBets() {
        LatestInfoFromAllPlayers latestInfoFromAllPlayers = new LatestInfoFromAllPlayers();
        GameModel gameModel = new GameModel();

        int totalSendBets = 0;
        int totalScoreOfThrees = 0;
        int totalScoreOfFours = 0;
        int totalScoreOfFives = 0;
        int totalScoreOfSixes = 0;

        for (int i = 1; i <= userRepository.findMaxId(); i++) {
            Optional<User> optionalUser =
                    userRepository.findById(i);

            if (optionalUser.isPresent() && historyGameRepository.amountRecords(i) > 1) {

                if (historyGameDtoService.getAllHistoryGames(i).get(0).getDateGame().equals(
                        latestInfoFromAllPlayersRepository.findDate()
                    )) {

                    totalSendBets += historyGameDtoService.getAllHistoryGames(i).get(0).getAmountBets();
                    totalScoreOfThrees += historyGameDtoService.getAllHistoryGames(i).get(0).getAmountGoalThrees();
                    totalScoreOfFours += historyGameDtoService.getAllHistoryGames(i).get(0).getAmountGoalFours();
                    totalScoreOfFives += historyGameDtoService.getAllHistoryGames(i).get(0).getAmountGoalFives();
                    totalScoreOfSixes += historyGameDtoService.getAllHistoryGames(i).get(0).getAmountGoalSixes();
                }
                if (historyGameDtoService.getAllHistoryGames(i).get(1).getDateGame().equals(
                        latestInfoFromAllPlayersRepository.findDate())) {

                    totalSendBets += historyGameDtoService.getAllHistoryGames(i).get(1).getAmountBets();
                    totalScoreOfThrees += historyGameDtoService.getAllHistoryGames(i).get(1).getAmountGoalThrees();
                    totalScoreOfFours += historyGameDtoService.getAllHistoryGames(i).get(1).getAmountGoalFours();
                    totalScoreOfFives += historyGameDtoService.getAllHistoryGames(i).get(1).getAmountGoalFives();
                    totalScoreOfSixes += historyGameDtoService.getAllHistoryGames(i).get(1).getAmountGoalSixes();
                }
             }
        }

        latestInfoFromAllPlayers.setDate(gameModel.getDateGame().get(0));
        latestInfoFromAllPlayers.setTotalSendBets(totalSendBets);
        latestInfoFromAllPlayers.setTotalScoreOfThrees(totalScoreOfThrees);
        latestInfoFromAllPlayers.setTotalScoreOfFours(totalScoreOfFours);
        latestInfoFromAllPlayers.setTotalScoreOfFives(totalScoreOfFives);
        latestInfoFromAllPlayers.setTotalScoreOfSixes(totalScoreOfSixes);
        latestInfoFromAllPlayersRepository.save(latestInfoFromAllPlayers);
    }
}
