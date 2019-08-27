package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.commons.dtos.mapper.UserMapper;
import kipoderax.virtuallotto.commons.dtos.models.UserDto;
import kipoderax.virtuallotto.game.entity.Game;
import kipoderax.virtuallotto.game.entity.UserExperience;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StatisticsServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;
    @InjectMocks
    StatisticsService statisticsService;

    private User user;
    private User user2;
    private User user3;
    private List<UserDto> users;

    @BeforeEach
    void setUp() {
        user = new User();
        user2 = new User();
        user3 = new User();
        users = new ArrayList<>();
    }

    @Test
    void getAllDtoUsers() {
        //given
        List<UserDto> dtoUsers = userDtoList();
        List<UserDto> sortedListByLevel;

        //when
        sortedListByLevel = statisticsService.getAllDtoUsers(dtoUsers);
        dtoUsers.sort(Comparator.comparing(UserDto::getExperience).reversed());

        //then
        assertThat(sortedListByLevel.get(0).getUsername()).isEqualTo("player1");
        assertThat(sortedListByLevel.get(1).getUsername()).isEqualTo("player3");
        assertThat(sortedListByLevel.size()).isEqualTo(3);

    }

    @Test
    void get5BestPlayersWithEmptyListShouldBeEmpty() {
        //given
        List<UserDto> users = emptyUserDtoList();
        given(statisticsService.getAllDtoUsers(new ArrayList<>())).willReturn(users);

        //when
        List<UserDto> list = statisticsService.get5BestPlayers();
        verify(userRepository).findAllOrderByLevel();

        //then
        assertThat(list.isEmpty()).isTrue();
    }

    @Test
    void get5BestPlayersWithNotEmptyListShouldBeNotEmpty() {
        //given
        List<UserDto> users = moreThanOneHundredUsers();
        given(statisticsService.get5BestPlayers()).willReturn(users);

        //when
//        List<UserDto> list = statisticsService.get5BestPlayers();
        verify(userRepository).findAllOrderByLevel();

//        InOrder inOrder = Mockito.inOrder(userRepository);
//        inOrder.verify(userRepository, times(1)).findAllOrderByLevel();

        //then
        assertThat(users.size()).isEqualTo(5);
    }

    @Test
    void get5PlayersWithLessThan5Players() {

    }

    @Test
    void MoreThanOneHundredUsersShouldReturnOneHundredUsers() {
        //given
        List<UserDto> users = moreThanOneHundredUsers();
        given(statisticsService.get100RecordsOfUsers()).willReturn(users.size());

        //when
        int amountUsers = statisticsService.get100RecordsOfUsers();
        //then
        assertThat(amountUsers).isEqualTo(100);
    }

    @Test
    void LessThanOneHundredUsersShouldReturnLessThanOneHundredUsers() {
        //given
        List<UserDto> users = lessThanOneHundredUsers();
        given(statisticsService.get100RecordsOfUsers()).willReturn(users.size());

        //when
        int amountUsers = statisticsService.get100RecordsOfUsers();

        //then
        assertThat(amountUsers).isEqualTo(50);
    }

    private List<User> userList() {
//        User user = new User();
//        User user2 = new User();
//        User user3 = new User();
        user.setUsername("player1");
        user2.setUsername("player2");
        user3.setUsername("player3");


        Game game = new Game();
        Game game2 = new Game();
        Game game3 = new Game();
        game.setNumberGame(189);
        game.setCountOfThree(5);
        game.setCountOfFour(0);
        game.setCountOfFive(0);
        game.setCountOfSix(0);
        game2.setNumberGame(160);
        game2.setCountOfThree(4);
        game2.setCountOfFour(0);
        game2.setCountOfFive(0);
        game2.setCountOfSix(0);
        game3.setNumberGame(165);
        game3.setCountOfThree(5);
        game3.setCountOfFour(0);
        game3.setCountOfFive(0);
        game3.setCountOfSix(0);

        UserExperience userExperience = new UserExperience();
        UserExperience userExperience2 = new UserExperience();
        UserExperience userExperience3 = new UserExperience();
        userExperience.setLevel(18);
        userExperience.setExperience(274);
        userExperience2.setLevel(17);
        userExperience2.setExperience(228);
        userExperience3.setLevel(17);
        userExperience3.setExperience(240);

        user.setGame(game);
        user.setUserExperience(userExperience);
        user2.setGame(game2);
        user2.setUserExperience(userExperience2);
        user3.setGame(game3);
        user3.setUserExperience(userExperience3);

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        return Arrays.asList(user, user2, user3);
    }

    private List<UserDto> userDtoList() {
        users = new ArrayList<>();

        for (int i = 0; i <= 2; i++) {
            UserDto userDto = new UserDto();
            userDto.setUsername(userList().get(i).getUsername());
            userDto.setNumberGame(userList().get(i).getGame().getNumberGame());
            userDto.setAmountOfThree(userList().get(i).getGame().getCountOfThree());
            userDto.setAmountOfFour(userList().get(i).getGame().getCountOfFour());
            userDto.setAmountOfFive(userList().get(i).getGame().getCountOfFive());
            userDto.setAmountOfSix(userList().get(i).getGame().getCountOfSix());
            userDto.setLevel(userList().get(i).getUserExperience().getLevel());
            userDto.setExperience(userList().get(i).getUserExperience().getExperience());
            users.add(userDto);
        }

        return users;
    }

    private List<UserDto> emptyUserDtoList() {

        return new ArrayList<>();
    }

    private List<UserDto> moreThanOneHundredUsers() {
        List<UserDto> users = new ArrayList<>();
        while (users.size() <= 110) {
            UserDto userDto = new UserDto();
            users.add(userDto);
        }

        return users;
    }

    private List<UserDto> lessThanOneHundredUsers() {
        List<UserDto> users = new ArrayList<>();
        while (users.size() < 50) {
            UserDto userDto = new UserDto();
            users.add(userDto);
        }

        return users;
    }


    @AfterEach
    void clearList() {
        userDtoList().clear();
    }

}