package kipoderax.virtuallotto.game.service;

import lombok.Data;
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

@Data
@Service
public class ConvertToJson {

    //todo zmienne lokalne w metodzie getJSON wyniesc na zewnatrz by moc je pobrac bardziej globalnie

    private List<Integer> lastLottoNumbers = new ArrayList<>();
    private Integer[] lastln = new Integer[15];

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

    //todo sprobowac za pomocą mapy
    public List<Integer> getLastWins(List<Integer> list) {
//        getJSON("https://app.lotto.pl/wyniki/?type=dl");

//        List<Integer> amountOfWinner = new ArrayList<>();
//        for (int i = 0; i < lastLottoNumbers.size(); i += 2) {
//            amountOfWinner.add(list.get(i));
//        }

        return list;
    }
}
