package kipoderax.virtuallotto.commons.displays;

import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.auth.service.SessionCounter;
import kipoderax.virtuallotto.game.model.GameModel;
import kipoderax.virtuallotto.game.repository.LatestInfoFromAllPlayersRepository;
import kipoderax.virtuallotto.game.service.StatisticsService;
import kipoderax.virtuallotto.game.service.dto.HistoryGameDtoService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.text.DecimalFormat;
import java.util.Collections;

@Service
public class MainPageDisplay {

    private StatisticsService statisticsService;
    private HistoryGameDtoService historyGameDtoService;
    private UserRepository userRepository;
    private LatestInfoFromAllPlayersRepository latestInfoFromAllPlayersRepository;

    public MainPageDisplay(StatisticsService statisticsService,
                           HistoryGameDtoService historyGameDtoService,
                           UserRepository userRepository,
                           LatestInfoFromAllPlayersRepository latestInfoFromAllPlayersRepository) {

        this.statisticsService = statisticsService;
        this.historyGameDtoService = historyGameDtoService;
        this.userRepository = userRepository;
        this.latestInfoFromAllPlayersRepository = latestInfoFromAllPlayersRepository;
    }

    public void displayGameStatus(Model model) {
        int allSendedBets = statisticsService.getAllSendedBets();
        int allThrees = statisticsService.getAllThrees();
        int allFours = statisticsService.getAllFours();
        int allFives = statisticsService.getAllFives();

        model.addAttribute("top5level", statisticsService.get5BestPlayers());
        model.addAttribute("toplastxp", historyGameDtoService.getLast5BestExperience());
        model.addAttribute("amountRegisterPlayers", userRepository.getAllRegisterUsers());
        model.addAttribute("sessionCounter", SessionCounter.getActiveSessions());
        model.addAttribute("allSendedBets", allSendedBets);
        model.addAttribute("allThrees", allThrees);
        model.addAttribute("allFours", allFours);
        model.addAttribute("allFives", allFives);
        model .addAttribute("percentThree", new DecimalFormat("##.##")
                .format(((double) allThrees/allSendedBets) * 100));
        model .addAttribute("percentFour", new DecimalFormat("##.##")
                .format(((double) allFours/allSendedBets) * 100));
        model .addAttribute("percentFive", new DecimalFormat("##.###")
                .format(((double) allFives/allSendedBets) * 100));
    }

    public void sumUpLatestPlayersGame(Model model) {

        model.addAttribute("sumUpDate", latestInfoFromAllPlayersRepository.findPreviousDate());
        model.addAttribute("totalSendBets", latestInfoFromAllPlayersRepository.findTotalSendBets());
        model.addAttribute("totalScoreOfThrees", latestInfoFromAllPlayersRepository.findTotalScoreOfThrees());
        model.addAttribute("totalScoreOfFours", latestInfoFromAllPlayersRepository.findTotalScoreOfFours());
        model.addAttribute("totalScoreOfFives", latestInfoFromAllPlayersRepository.findTotalScoreOfFives());
        model.addAttribute("totalScoreOfSixes", latestInfoFromAllPlayersRepository.findTotalScoreOfSixes());
    }

    public void drawNumbers(Model model, GameModel gameModel) {
        String date = gameModel.getDateGame().subList(0, 1).toString()
                .substring(1, 11).replace('-', '.');
        model.addAttribute("date", date);

        Collections.sort(gameModel.getLastNumbers().subList(0, 6));

        model.addAttribute("result", gameModel.getLastNumbers().subList(0, 6));
        model.addAttribute("fourth", gameModel.getRewardsMoney()[2]);
        model.addAttribute("fifth", gameModel.getRewardsMoney()[3]);
        model.addAttribute("sixth", gameModel.getRewardsMoney()[4]);

    }
}
