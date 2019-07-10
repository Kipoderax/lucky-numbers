package kipoderax.virtuallotto.game.service;

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
    private List<String> date = new ArrayList<>();

    private List<String> moneyRewards = new ArrayList<>();
    private int[] moneyRew = new int[4];

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

                        moneyRewards.add(line);

                        try {

                            lottoNumber = Integer.parseInt(line);
                        }
                        catch (NumberFormatException e) { continue; }

                        lastLottoNumbers.add(lottoNumber);
                    }
//                    date.append(sb,0, 10);
//                    date = sb.append(sb, 0, 10).toString();//.toString().substring(0, 10);
                    date.add(sb.toString().substring(0, 10));

                    int fourthPrice = (int) Double.parseDouble(getMoneyRewards().get(3).substring(5, 7));
                    int fifthPrice = (int) Double.parseDouble(getMoneyRewards().get(2).substring(3));
                    int sixthPrice = (int) Double.parseDouble(getMoneyRewards().get(1).substring(2));

                    moneyRew[1] = fourthPrice;
                    moneyRew[2] = fifthPrice;
                    moneyRew[3] = sixthPrice;

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

    public List<String> getLastDate(List<String> date) {
        getJSON("https://app.lotto.pl/wyniki/?type=dl");

        return date;
    }

    public int getLastWins(int number) {
        getJSON("https://app.lotto.pl/wygrane/?type=dl");

        return number;
    }
}
