package kipoderax.virtuallotto.dtos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class UserDto {

    private String username;
    private int numberGame;
    private int amountOfThree;
    private int amountOfFour;
    private int amountOfFive;
    private int amountOfSix;
    private int level;
    private int experience;

}