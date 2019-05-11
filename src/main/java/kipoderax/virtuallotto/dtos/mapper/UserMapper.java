package kipoderax.virtuallotto.dtos.mapper;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.dtos.models.UserDto;
import kipoderax.virtuallotto.dtos.models.UserDtoBuilder;
import kipoderax.virtuallotto.game.entity.Game;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class UserMapper implements Mapper<User, UserDto> {

    /*todo albo zrobic metode apply dla kazdego pola potrzebnego do statystyk lub zmienic relacje na OneToOne
    todo do bezposredniego dostępu do pola*/

    @Override
    public UserDto map(User from) {

        List<List<Integer>> amountFrom3To6 = from.getGame()
                .stream()
                .map(GameToList.INSTANE)
                .collect(Collectors.toList());

        return null;
    }

    private enum GameToList implements Function<Game, List<Integer>> {
        INSTANE;

        @Override
        public List<Integer> apply(Game game) {
            List<Integer> fieldsFromGame = new ArrayList<>();
            fieldsFromGame.add(game.getCountOfThree());
            fieldsFromGame.add(game.getCountOfFour());
            fieldsFromGame.add(game.getCountOfFive());
            fieldsFromGame.add(game.getCountOfSix());
            fieldsFromGame.add(game.getNumberGame());
            return fieldsFromGame;
        }
    }

}