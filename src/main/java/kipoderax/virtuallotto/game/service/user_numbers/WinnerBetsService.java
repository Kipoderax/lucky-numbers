package kipoderax.virtuallotto.game.service.user_numbers;

import kipoderax.virtuallotto.commons.dtos.models.LottoNumbersDto;

import java.util.List;

public interface WinnerBetsService{

    void addWinnerBetsWith3Numbers(LottoNumbersDto lottoNumbersDto);
    void addWinnerBetsWith4Numbers(LottoNumbersDto lottoNumbersDto);
    void addWinnerBetsWith5Numbers(LottoNumbersDto lottoNumbersDto);
    void addWinnerBetsWith6Numbers(LottoNumbersDto lottoNumbersDto);

    List<LottoNumbersDto> getWinnerBetsWith3Numbers();
    List<LottoNumbersDto> getWinnerBetsWith4Numbers();
    List<LottoNumbersDto> getWinnerBetsWith5Numbers();
    List<LottoNumbersDto> getWinnerBetsWith6Numbers();

}
