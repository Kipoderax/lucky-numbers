package kipoderax.virtuallotto.commons.dtos.mapper;

import kipoderax.virtuallotto.commons.dtos.models.LottoNumbersDto;
import kipoderax.virtuallotto.commons.dtos.models.NumbersDtoBuilder;
import kipoderax.virtuallotto.game.entity.UserBets;
import org.springframework.stereotype.Component;

@Component
public class BetNumbersMapper implements Mapper<UserBets, LottoNumbersDto>{

    @Override
    public LottoNumbersDto map(UserBets from) {
        return new NumbersDtoBuilder()
                .number1(from.getNumber1())
                .number2(from.getNumber2())
                .number3(from.getNumber3())
                .number4(from.getNumber4())
                .number5(from.getNumber5())
                .number6(from.getNumber6())
                .build();
    }
}
