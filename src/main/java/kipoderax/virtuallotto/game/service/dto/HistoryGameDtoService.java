package kipoderax.virtuallotto.game.service.dto;

import kipoderax.virtuallotto.auth.repositories.HistoryGameRepository;
import kipoderax.virtuallotto.commons.dtos.mapper.HistoryMapper;
import kipoderax.virtuallotto.commons.dtos.models.HistoryGameDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryGameDtoService {

    HistoryGameRepository historyGameRepository;
    HistoryMapper historyMapper;

    public HistoryGameDtoService(HistoryGameRepository historyGameRepository, HistoryMapper historyMapper) {
        this.historyGameRepository = historyGameRepository;
        this.historyMapper = historyMapper;
    }

    public List<HistoryGameDto> getAllHistoryGames(int userId) {
        List<HistoryGameDto> historyGameDtos = new ArrayList<>();

        historyGameRepository.findAllById(userId)
                .stream()
                .map(n -> historyGameDtos.add(historyMapper.map(n)))
                .collect(Collectors.toList());

        return historyGameDtos;
    }
}
