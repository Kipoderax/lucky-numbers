package kipoderax.virtuallotto.game.service.user_numbers;

import kipoderax.virtuallotto.commons.dtos.models.LottoNumbersDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WinnerBetsServiceImplTest {

    LottoNumbersDto lottoNumbersDto;
    private List<LottoNumbersDto> with3Numbers;
    private List<LottoNumbersDto> with4Numbers;
    private List<LottoNumbersDto> with5Numbers;
    private List<LottoNumbersDto> with6Numbers;

    @BeforeEach
    void setUp() {

        lottoNumbersDto = new LottoNumbersDto(11, 13, 17, 21 ,24, 31);
        with6Numbers = new ArrayList<>();
        with6Numbers.add(lottoNumbersDto);

    }

    @Test
    void addWinnerBetsWith3Numbers() {
    }

    @Test
    void addWinnerBetsWith4Numbers() {
    }

    @Test
    void addWinnerBetsWith5Numbers() {
    }

    @Test
    void addWinnerBetsWith6Numbers() {

    }

    @Test
    void getWinnerBetsWith3Numbers() {
    }

    @Test
    void getWinnerBetsWith4Numbers() {
    }

    @Test
    void getWinnerBetsWith5Numbers() {
    }

    @Test
    void getWinnerBetsWith6Numbers() {

        assertThat(with6Numbers.size()).isEqualTo(1);
    }
}