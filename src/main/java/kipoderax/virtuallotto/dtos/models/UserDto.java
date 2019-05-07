package kipoderax.virtuallotto.dtos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class UserDto {

    private String username;
    private String numberGame;
    private String amountOfThree;
    private String amountOfFour;
    private String amountOfFive;
    private String amountOfSix;
    private String level;

}