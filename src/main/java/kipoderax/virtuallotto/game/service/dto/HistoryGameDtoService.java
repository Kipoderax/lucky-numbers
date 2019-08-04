package kipoderax.virtuallotto.game.service.dto;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.repositories.HistoryGameRepository;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.commons.dtos.mapper.HistoryMapper;
import kipoderax.virtuallotto.commons.dtos.models.HistoryGameDto;
import kipoderax.virtuallotto.game.model.GameModel;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistoryGameDtoService {

    private HistoryGameRepository historyGameRepository;
    private HistoryMapper historyMapper;
    private UserRepository userRepository;

    public HistoryGameDtoService(HistoryGameRepository historyGameRepository,
                                 UserRepository userRepository,
                                 HistoryMapper historyMapper) {

        this.historyGameRepository = historyGameRepository;
        this.historyMapper = historyMapper;
        this.userRepository = userRepository;
    }

    public List<HistoryGameDto> putAllHistoryGames(int userId, List<HistoryGameDto> historyGameDtos) {

        historyGameRepository.findAllById(userId)
                .stream()
                .map(n -> historyGameDtos.add(historyMapper.map(n)))
                .collect(Collectors.toList());

        Collections.reverse(historyGameDtos);

        return historyGameDtos;
    }

    public List<HistoryGameDto> getAllHistoryGames(int userId) {
        List<HistoryGameDto> historyGameDtos = new ArrayList<>();

        putAllHistoryGames(userId, historyGameDtos);

        return historyGameDtos;
    }

    public List<HistoryGameDto> getLast5BestExperience() {
        List<HistoryGameDto> historyGameDtos = new ArrayList<>();
        List<HistoryGameDto> getExperience;
        GameModel gameModel = new GameModel();

        if (userRepository.findMaxId() != null) {
            System.out.println("maxId: " + userRepository.findMaxId());
            for (int j = 1; j <= userRepository.findMaxId(); j++) {

                putAllHistoryGames(j, historyGameDtos);
            }
        }

        getExperience = historyGameDtos.stream()
                .filter(n -> n.getDateGame().equals(gameModel.getDateGame().get(0)))
                .sorted(Comparator.comparing(HistoryGameDto::getExperience).reversed())
//                .limit(5)
                .collect(Collectors.toList());

        if (getExperience.size() < 5) {
            return getExperience;
        }
        return getExperience.subList(0, 5);
    }
}
