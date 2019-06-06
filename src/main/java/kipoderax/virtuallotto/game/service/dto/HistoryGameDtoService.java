package kipoderax.virtuallotto.game.service.dto;

import kipoderax.virtuallotto.auth.entity.HistoryGame;
import kipoderax.virtuallotto.auth.repositories.HistoryGameRepository;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.commons.dtos.mapper.HistoryMapper;
import kipoderax.virtuallotto.commons.dtos.models.HistoryGameDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoryGameDtoService {

    HistoryGameRepository historyGameRepository;
    HistoryMapper historyMapper;
    UserRepository userRepository;

    public HistoryGameDtoService(HistoryGameRepository historyGameRepository,
                                 UserRepository userRepository,
                                 HistoryMapper historyMapper) {

        this.historyGameRepository = historyGameRepository;
        this.historyMapper = historyMapper;
        this.userRepository = userRepository;
    }

    public List<HistoryGameDto> getAllHistoryGames(int userId) {
        List<HistoryGameDto> historyGameDtos = new ArrayList<>();

        historyGameRepository.findAllById(userId)
                .stream()
                .map(n -> historyGameDtos.add(historyMapper.map(n)))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        System.out.println(historyGameDtos.get(historyGameDtos.size() - 1));

        return historyGameDtos;
    }

    public List<HistoryGameDto> getLast5BestExperience() {
        List<HistoryGameDto> historyGameDtos = new ArrayList<>();

//        for (int i = 1; i < ; i++) {
//            Optional<HistoryGame> historyGame = historyGameRepository.findById(i);
//
//            if (!historyGame.isPresent()) {
//                continue;
//            } else {
//                historyGameDtos.add(getAllHistoryGames(i).get(0));
//            }
//        }


//        System.out.println(historyGameDtos.get(1).getExperience());
        return historyGameDtos;
    }
}
