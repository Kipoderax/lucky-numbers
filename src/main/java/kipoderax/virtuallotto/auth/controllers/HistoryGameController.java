package kipoderax.virtuallotto.auth.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistoryGameController {

    @GetMapping("/history")
    public String historyGame() {

        return "auth/history-game";
    }
}
