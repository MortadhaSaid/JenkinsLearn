package tn.esprit.cloudwizards.connect4aid.services.EmailScheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.Month;
@Component
public class ScheduledMails {
    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "00 33 14 * * *")
    public void sendEmailBeforeSetDate() throws MessagingException {
        LocalDateTime setDateTime = LocalDateTime.of(2023, Month.APRIL, 2, 0, 0); // replace with your set date
        LocalDateTime now = LocalDateTime.now();
       if (now.plusDays(4).isEqual(setDateTime) || now.plusDays(4).isAfter(setDateTime)) {
            EmailDetails E = new EmailDetails(
            "mortadha.said@esprit.tn",
            "Scheduled Email test 2",
           "Hello, this email was sent using Spring Boot's Task Scheduling 2 !",null);
            emailService.sendSimpleMail(E);
        }
    }

}
