package kipoderax.virtuallotto.commons.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GamesDto {

    @JsonProperty("Lotto")
    private Lotto lotto;
    @JsonProperty("Mini")
    private Mini miniLotto;
    @JsonProperty("EJ")
    private EJ euroJackpot;

    @Data
    public static class Lotto {
        String numerki;
    }

    public static class Mini {

    }

    public static class EJ {

    }
}
