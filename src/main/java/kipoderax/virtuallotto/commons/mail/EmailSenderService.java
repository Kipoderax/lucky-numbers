package kipoderax.virtuallotto.commons.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
@PropertySource(value={"classpath:config.properties"})
public class EmailSenderService implements EmailSender {

    private JavaMailSender javaMailSender;

    @Value("${email.from}")
    private String from;

    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(Email email) {
        MimeMessage mail = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setTo(email.getAddress());
            helper.setFrom(from);
            helper.setSubject(email.getSubject());
            helper.setText(email.getBody(), true);

        } catch (MessagingException e) {

            e.printStackTrace();
        }

        try {

            javaMailSender.send(mail);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public Long tokenRemaining(Date currentTime, Date tokenTime) {

        return currentTime.getTime() - tokenTime.getTime();
    }
}
