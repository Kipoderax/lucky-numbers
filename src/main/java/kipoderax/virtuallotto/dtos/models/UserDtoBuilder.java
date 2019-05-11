package kipoderax.virtuallotto.dtos.models;

public class UserDtoBuilder {

    private String name = "";
    private int amountOfThree = 0;
    private int amountOfFour = 0;
    private int amountOfFive = 0;
    private int amountOfSix = 0;
    private int numberGame = 0;
    private int level = 0;
    private int experience = 0;

    public UserDtoBuilder() {
    }

    public UserDtoBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserDtoBuilder amountOfThree(int amountOfThree) {
        this.amountOfThree = amountOfThree;
        return this;
    }

    public UserDtoBuilder amountOfFour(int amountOfFour) {
        this.amountOfFour = amountOfFour;
        return this;
    }

    public UserDtoBuilder amountOfFive(int amountOfFive) {
        this.amountOfFive = amountOfFive;
        return this;
    }

    public UserDtoBuilder amountOfSix(int amountOfSix) {
        this.amountOfSix = amountOfSix;
        return this;
    }

    public UserDtoBuilder numberGame(int numberGame) {
        this.numberGame = numberGame;
        return this;
    }

    public UserDtoBuilder level(int level) {
        this.level = level;
        return this;
    }

    public UserDtoBuilder experience(int experience) {
        this.experience = experience;
        return this;
    }

    public UserDto build() {
        return new UserDto(name, amountOfThree, amountOfFour, amountOfFive, amountOfSix, numberGame, level, experience);
    }
}
