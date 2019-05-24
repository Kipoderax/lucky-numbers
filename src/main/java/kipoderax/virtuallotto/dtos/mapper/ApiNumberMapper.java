package kipoderax.virtuallotto.dtos.mapper;

import kipoderax.virtuallotto.dtos.models.ApiNumberDto;
import kipoderax.virtuallotto.dtos.models.ApiNumberDtoBuilder;
import kipoderax.virtuallotto.game.entity.ApiNumber;
import org.springframework.stereotype.Component;

@Component
public class ApiNumberMapper implements Mapper<ApiNumber, ApiNumberDto>{

    public ApiNumberDto map(ApiNumber from) {
        return new ApiNumberDtoBuilder()
                .number1(from.getNumber1())
                .number2(from.getNumber2())
                .number3(from.getNumber3())
                .number4(from.getNumber4())
                .number5(from.getNumber5())
                .number6(from.getNumber6())
                .build();
    }
}
