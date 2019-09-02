package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.auth.entity.User;
import kipoderax.virtuallotto.auth.repositories.UserRepository;
import kipoderax.virtuallotto.commons.dtos.mapper.UserMapper;
import kipoderax.virtuallotto.commons.dtos.models.UserDto;
import kipoderax.virtuallotto.game.entity.Game;
import kipoderax.virtuallotto.game.entity.UserExperience;
import kipoderax.virtuallotto.tags.ServiceTests;
import org.hibernate.engine.jdbc.connections.internal.DriverConnectionCreator;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@RunWith(SpringRunner.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StatisticsServiceTest implements ServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private StatisticsService statisticsService;

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
        List<UserDto> dtoUsers = new ArrayList<>();
        List<User> users = userList();
        given(userRepository.findAllOrderByLevel()).willReturn(users);

        //when
        statisticsService.getAllDtoUsers(dtoUsers);

        //then
        verify(userRepository).findAllOrderByLevel();
        verify(userMapper).map(users.get(0));
        assertThat(dtoUsers.size()).isEqualTo(3);
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
    void get5BestPlayersWithMoreThan5PlayersShouldReturn5Players() {
        //given
        List<UserDto> userDtoList = new ArrayList<>();
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userDtoList.add(new UserDto());
            userList.add(new User());
        }
        given(statisticsService.getAllDtoUsers(new ArrayList<>())).willReturn(userDtoList);
        given(userRepository.findAllOrderByLevel()).willReturn(userList);

        //when

        statisticsService.get5BestPlayers();
        int sizeUserList = statisticsService.get5BestPlayers().size();

        //then
        verify(userRepository, times(2)).findAllOrderByLevel();
        assertThat(sizeUserList).isEqualTo(5);
    }

    @Test
    void get5BestPlayersWithLessThan5PlayersShouldReturnLessThan5Players() {
        //given
        List<UserDto> userDtoList2 = new ArrayList<>();
        List<User> userList2 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            userDtoList2.add(new UserDto());
            userList2.add(new User());
        }
        given(statisticsService.getAllDtoUsers(new ArrayList<>())).willReturn(userDtoList2);
        given(userRepository.findAllOrderByLevel()).willReturn(userList2);

        //when

        statisticsService.get5BestPlayers();
        int sizeUserList2 = statisticsService.get5BestPlayers().size();

        //then
        assertThat(sizeUserList2).isEqualTo(3);
    }

    @Test
    public void getAllDtoUsersDefaultWithEmptyListShouldReturnEmptyList() {
        //when
        List<UserDto> users = statisticsService.getAllDtoUsersDefault();

        //then
        assertThat(users).isEmpty();
    }

    @Test
    public void getAllDtoUsersDefaultWithLessThan100UsersShouldReturnLessThan100Users() {
        //given
        List<UserDto> userDtoList = userDtoList();
        List<User> userList = userList();

        given(userRepository.findAllOrderByLevel()).willReturn(userList);
        given(statisticsService.getAllDtoUsersDefault()).willReturn(userDtoList);

        //when
        statisticsService.getAllDtoUsersDefault();

        //then
        assertThat(userDtoList.size()).isEqualTo(3);
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

    @Test
    public void sortByExperience() {
        //given
        List<UserDto> userDtoList = userDtoList();

        //when
        statisticsService.sortByExperience(userDtoList);

        //then
        assertThat(userDtoList.get(1).getUsername()).isEqualTo("player3");
    }

    @Test
    public void sortByNumberGame() {
        //given
        List<UserDto> userDtoList = userDtoList();

        //when
        statisticsService.sortByNumberGame(userDtoList);

        //then
        assertThat(userDtoList.get(1).getUsername()).isEqualTo("player3");
    }

    @Test
    public void sortByAmountGoalNumbers() {
        //given
        List<UserDto> userDtoList = userDtoList();

        //when goal three numbers
        statisticsService.sortByAmountOfGoalNumbers(userDtoList, Comparator.comparing(UserDto::getAmountOfThree));

        //then should return player3
        assertThat(userDtoList.get(0).getUsername()).isEqualTo("player3");

        //when goal four numbers
        statisticsService.sortByAmountOfGoalNumbers(userDtoList, Comparator.comparing(UserDto::getAmountOfFour));

        //then should return player2
        assertThat(userDtoList.get(0).getUsername()).isEqualTo("player2");

        //when goal five numbers
        statisticsService.sortByAmountOfGoalNumbers(userDtoList, Comparator.comparing(UserDto::getAmountOfFive));

        //then should return player2
        assertThat(userDtoList.get(0).getUsername()).isEqualTo("player2");

        //when goal six numbers
        statisticsService.sortByAmountOfGoalNumbers(userDtoList, Comparator.comparing(UserDto::getAmountOfSix));

        //then should return player2
        assertThat(userDtoList.get(0).getUsername()).isEqualTo("player3");
    }

    private List<User> userList() {
        user.setUsername("player1");
        user2.setUsername("player2");
        user3.setUsername("player3");

        Game game = new Game();
        Game game2 = new Game();
        Game game3 = new Game();
        game.setNumberGame(189);
        game.setCountOfThree(5);
        game.setCountOfFour(1);
        game.setCountOfFive(0);
        game.setCountOfSix(0);
        game2.setNumberGame(160);
        game2.setCountOfThree(4);
        game2.setCountOfFour(3);
        game2.setCountOfFive(1);
        game2.setCountOfSix(0);
        game3.setNumberGame(165);
        game3.setCountOfThree(6);
        game3.setCountOfFour(2);
        game3.setCountOfFive(0);
        game3.setCountOfSix(1);

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