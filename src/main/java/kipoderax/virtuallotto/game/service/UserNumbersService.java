package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.dtos.mapper.UserNumbersMapper;
import kipoderax.virtuallotto.dtos.models.UserNumbersDto;
import kipoderax.virtuallotto.game.entity.UserBets;
import kipoderax.virtuallotto.game.repository.UserBetsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserNumbersService {

    private UserNumbersMapper userNumbersMapper;
    private UserBetsRepository userBetsRepository;

    public UserNumbersService(UserNumbersMapper userNumbersMapper, UserBetsRepository userBetsRepository) {
        this.userNumbersMapper = userNumbersMapper;
        this.userBetsRepository = userBetsRepository;
    }


    public List<UserNumbersDto> userNumbersDtos(List<UserNumbersDto> userNumbersDtos, int userId) {

        userBetsRepository.findAllById(userId).stream()
                .map(n -> userNumbersDtos.add(userNumbersMapper.map(n)))
                .collect(Collectors.toList());

        return userNumbersDtos;
    }

    public List<UserNumbersDto> getAllUserNumbersById(int userId) {
        List<UserNumbersDto> userNumbersDtos = new ArrayList<>();

        userNumbersDtos(userNumbersDtos, userId);

        return userNumbersDtos;
    }

    public void checkUserNumbers (int betsId, int userId) {
        List<UserNumbersDto> userNumbersDtos = new ArrayList<>();
        userNumbersDtos(userNumbersDtos, userId);

        Optional<UserBets> userBetsOptional = userBetsRepository.findByUserId(userId);

        for(int i = 1; i <= betsId; i++) {
            if (userBetsOptional.isPresent()) {



            }
        }

    }

}
