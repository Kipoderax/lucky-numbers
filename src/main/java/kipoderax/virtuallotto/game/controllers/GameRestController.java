package kipoderax.virtuallotto.game.controllers;

import kipoderax.virtuallotto.game.service.ConvertToJson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/game/")
public class GameRestController {

    ConvertToJson convertToJson;

    public GameRestController(ConvertToJson convertToJson) {
        this.convertToJson = convertToJson;
    }

    @GetMapping("last-result")
    public List<Integer> getLastResult() {

        return convertToJson.getLastNumbers(convertToJson.getLastLottoNumbers());
    }

    @GetMapping("last-wins")
    public String getWins() {

        return convertToJson.getJSON("https://app.lotto.pl/wygrane/?type=dl");
    }

}
