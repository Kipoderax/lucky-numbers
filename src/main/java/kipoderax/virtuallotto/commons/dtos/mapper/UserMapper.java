package kipoderax.virtuallotto.commons.dtos.mapper;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.commons.dtos.models.UserDto;
import kipoderax.virtuallotto.commons.dtos.models.UserDtoBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserDto> {

    @Override
    public UserDto map(User from) {

        return new UserDtoBuilder()
                .name(from.getUsername())
                .numberGame(from.getGame().getNumberGame())
                .amountOfThree(from.getGame().getCountOfThree())
                .amountOfFour(from.getGame().getCountOfFour())
                .amountOfFive(from.getGame().getCountOfFive())
                .amountOfSix(from.getGame().getCountOfSix())
                .level(from.getUserExperience().getLevel())
                .experience(from.getUserExperience().getExperience())
                .build();
    }
}