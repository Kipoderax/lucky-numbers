package kipoderax.virtuallotto.commons.dtos.mapper;

import kipoderax.virtuallotto.commons.dtos.models.UserNumbersDto;
import kipoderax.virtuallotto.commons.dtos.models.UserNumbersDtoBuilder;
import kipoderax.virtuallotto.game.entity.UserBets;
import org.springframework.stereotype.Component;

@Component
public class UserNumbersMapper implements Mapper<UserBets, UserNumbersDto>{

    @Override
    public UserNumbersDto map(UserBets from) {
        return new UserNumbersDtoBuilder()
                .number1(from.getNumber1())
                .number2(from.getNumber2())
                .number3(from.getNumber3())
                .number4(from.getNumber4())
                .number5(from.getNumber5())
                .number6(from.getNumber6())
                .build();
    }
}
