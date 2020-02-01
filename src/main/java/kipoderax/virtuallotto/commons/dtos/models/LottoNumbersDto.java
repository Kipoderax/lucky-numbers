package kipoderax.virtuallotto.commons.dtos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data @AllArgsConstructor @NoArgsConstructor
public class LottoNumbersDto {

    @Range(min = 0, max = 49)
    private int number1;
    @Range(min = 0, max = 49)
    private int number2;
    @Range(min = 0, max = 49)
    private int number3;
    @Range(min = 0, max = 49)
    private int number4;
    @Range(min = 0, max = 49)
    private int number5;
    @Range(min = 0, max = 49)
    private int number6;

}
