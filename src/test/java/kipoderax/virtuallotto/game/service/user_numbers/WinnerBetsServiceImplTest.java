package kipoderax.virtuallotto.game.service.user_numbers;

import kipoderax.virtuallotto.commons.dtos.models.LottoNumbersDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WinnerBetsServiceImplTest {

    private LottoNumbersDto lottoNumbersDto;

    @BeforeEach
    void setUp() {
        //given
        lottoNumbersDto = new LottoNumbersDto();
        lottoNumbersDto.setNumber1(10);
        lottoNumbersDto.setNumber2(13);
        lottoNumbersDto.setNumber3(21);
        lottoNumbersDto.setNumber4(26);
        lottoNumbersDto.setNumber5(40);
        lottoNumbersDto.setNumber6(45);
    }

    @InjectMocks
    private WinnerBetsServiceImpl winnerBetsServiceImpl;

    @Test
    void getWinnerBetsWith3Numbers() {
        //when
        winnerBetsServiceImpl.addWinnerBetsWith3Numbers(lottoNumbersDto);

        //then
        assertThat(winnerBetsServiceImpl.getWinnerBetsWith3Numbers().size()).isEqualTo(1);

    }

    @Test
    void getWinnerBetsWith4Numbers() {
        //when
        winnerBetsServiceImpl.addWinnerBetsWith4Numbers(lottoNumbersDto);

        //then
        assertThat(winnerBetsServiceImpl.getWinnerBetsWith4Numbers().size()).isEqualTo(1);
    }

    @Test
    void getWinnerBetsWith5Numbers() {
        //when
        winnerBetsServiceImpl.addWinnerBetsWith5Numbers(lottoNumbersDto);

        //then
        assertThat(winnerBetsServiceImpl.getWinnerBetsWith5Numbers().size()).isEqualTo(1);
    }

    @Test
    void getWinnerBetsWith6Numbers() {
        //when
        winnerBetsServiceImpl.addWinnerBetsWith6Numbers(lottoNumbersDto);

        //then
        assertThat(winnerBetsServiceImpl.getWinnerBetsWith6Numbers().size()).isEqualTo(1);
    }
}