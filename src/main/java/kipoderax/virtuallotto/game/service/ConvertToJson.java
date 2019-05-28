package kipoderax.virtuallotto.game.service;

import kipoderax.virtuallotto.game.model.GameModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Data @NoArgsConstructor
@Service
public class ConvertToJson {

    private List<Integer> lastLottoNumbers = new ArrayList<>();
    private StringBuilder date = new StringBuilder();

    public String getJSON(String url) {

        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");

                        int lottoNumber;

                        try {
                            lottoNumber = Integer.parseInt(line);

                        }
                        catch (NumberFormatException e) { continue; }

                        lastLottoNumbers.add(lottoNumber);

                    }
//                    date = sb.substring(0, 10);
                    date.append(sb,0, 10);

                    br.close();

                    return sb.toString();
            }

        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public List<Integer> getLastNumbers(List<Integer> list) {
        getJSON("https://app.lotto.pl/wyniki/?type=dl");

        return list;
    }

    public StringBuilder getLastDate(StringBuilder date) {
        getJSON("https://app.lotto.pl/wyniki/?type=dl");
        System.out.println("getLastDate: " + date);

        return date;
    }

    public List<Integer> getLastWins(List<Integer> list) {
        getJSON("https://app.lotto.pl/wygrane/?type=dl");

        return list;
    }
}
