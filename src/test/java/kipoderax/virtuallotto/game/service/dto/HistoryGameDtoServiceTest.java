package kipoderax.virtuallotto.game.service.dto;

import kipoderax.virtuallotto.auth.entity.HistoryGame;
import kipoderax.virtuallotto.auth.repositories.HistoryGameRepository;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.commons.dtos.mapper.HistoryMapper;
import kipoderax.virtuallotto.commons.dtos.models.HistoryGameDto;
import kipoderax.virtuallotto.tags.ServiceTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@RunWith(SpringRunner.class)
class HistoryGameDtoServiceTest implements ServiceTests {

    private @Mock HistoryGameRepository historyGameRepository;
    private @Mock UserRepository userRepository;
    private @Mock HistoryMapper historyMapper;

    @InjectMocks
    private HistoryGameDtoService historyGameDtoService;

    private int userId;
    private List<HistoryGame> historyGames;
    private List<HistoryGameDto> historyGameDtos;
    @BeforeEach
    void setUp() {
        userId = 15;
        historyGames = dataHistoryGames();
        historyGameDtos = new ArrayList<>();
    }

    @Test
    public void putAllHistoryGames() {
        //given
        given(historyGameRepository.findAllById(anyInt())).willReturn(historyGames);

        //when
        historyGameDtoService.putAllHistoryGames(userId, historyGameDtos);

        //then
        verify(historyGameRepository).findAllById(userId);
        verify(historyMapper, times(1)).map(historyGames.get(1));
    }

    private List<HistoryGame> dataHistoryGames() {
        List<HistoryGame> historyGames = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            HistoryGame historyGame = new HistoryGame();
            historyGame.setHistoryGameId(i+1);
            historyGame.setAmountBets(5 + (i*5));
            historyGame.setAmountGoalThrees(((i+1)*2));
            historyGame.setAmountGoalFours(5 - (i));
            historyGame.setAmountGoalFives(1);
            historyGame.setAmountGoalSixes(0);
            historyGame.setExperience(50 + (i*25));
            historyGame.setResult(18 - (i*10));

            historyGames.add(historyGame);
        }

        return historyGames;
    }

}