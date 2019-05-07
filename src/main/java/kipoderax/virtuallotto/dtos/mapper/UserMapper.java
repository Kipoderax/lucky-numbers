package kipoderax.virtuallotto.dtos.mapper;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.dtos.models.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<User, UserDto> {

//    @Override
//    public UserDto map(User from) {
//        return new UserDto(
//                from.getUsername(),
//
//
//    }


    @Override
    public UserDto map(User from) {
        return null;
    }
}