package kipoderax.virtuallotto.commons.api;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GamesDtoService {

    public GamesDto getGames() {
        GamesDto gamesDto = getRestTemplate().getForObject("http://serwis.mobilotto.pl/mapi_v6/index.php?json=getGames", GamesDto.class);

        return gamesDto;
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
