package kipoderax.virtuallotto.commons.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidation {

    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

    public static final String EMAIL_PATTERN = "^[a-zA-z0-9]+[\\._a-zA-Z0-9]*@[a-zA-Z0-9]+{2,}\\.[a-zA-Z]{2,}[\\.a-zA-Z0-9]*$";

    public static final String USERNAME_PATTERN = "^(?=.*[a-zA-Z]).{3,16}";

    public static boolean isCorrectPattern(String pattern, String pStr) {

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(pStr);
        return m.matches();
    }


}
