package kipoderax.virtuallotto.game.service.user_numbers;

import kipoderax.virtuallotto.commons.dtos.models.LottoNumbersDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WinnerBetsServiceImpl implements WinnerBetsService {

    private List<LottoNumbersDto> with3Numbers = new ArrayList<>();
    private List<LottoNumbersDto> with4Numbers = new ArrayList<>();
    private List<LottoNumbersDto> with5Numbers = new ArrayList<>();
    private List<LottoNumbersDto> with6Numbers = new ArrayList<>();

    @Override
    public void addWinnerBetsWith3Numbers(LottoNumbersDto lottoNumbersDto) {
        this.with3Numbers.add(lottoNumbersDto);
    }

    @Override
    public void addWinnerBetsWith4Numbers(LottoNumbersDto lottoNumbersDto) {
        this.with4Numbers.add(lottoNumbersDto);
    }

    @Override
    public void addWinnerBetsWith5Numbers(LottoNumbersDto lottoNumbersDto) {
        this.with5Numbers.add(lottoNumbersDto);
    }

    @Override
    public void addWinnerBetsWith6Numbers(LottoNumbersDto lottoNumbersDto) {
        this.with6Numbers.add(lottoNumbersDto);
    }

    @Override
    public List<LottoNumbersDto> getWinnerBetsWith3Numbers() {
        return with3Numbers;
    }

    @Override
    public List<LottoNumbersDto> getWinnerBetsWith4Numbers() {
        return with4Numbers;
    }

    @Override
    public List<LottoNumbersDto> getWinnerBetsWith5Numbers() {
        return with5Numbers;
    }

    @Override
    public List<LottoNumbersDto> getWinnerBetsWith6Numbers() {
        return with6Numbers;
    }
}
