package com.candyquizz.event.listener;

import com.candyquizz.user.User;
import com.candyquizz.event.RegistrationCompleteEvent;
import com.candyquizz.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
 private final UserService userService;

 private final JavaMailSender mailSender;
 private User theUser;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        theUser = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        userService.saveUserVerificationToken(theUser, verificationToken);
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Cliquez sur le lien pour vérifier votre inscription :  {}", url);
    }
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Verification d'email";
        String senderName = "Candy Quizz";
        String mailContent = "<p> Hey, "+ theUser.getFirstName()+ ", </p>"+
                "<p>merci pour votre souscription,"+"" +
                "merci de suivre les instructions pour finaliser votre inscription.</p>"+
                "<a href=\"" +url+ "\">Verifiez votre email pour activer votre compte</a>"+
                "<p> Merci <br> Simohamed Cainallah";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("yanissvaljean@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

    public void sentPasswordResetVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Réinsialiser votre mot de passe";
        String senderName = "Candy Quizz";
        String mailContent = "<p> Hey, "+ theUser.getFirstName()+ ", </p>"+
                "<p>Vous avez demandé à reinsialiser votre mot de passe,"+"" +
                "Merci de suivre les instructions.</p>"+
                "<a href=\"" +url+ "\">Réinsialiser le mot de passe</a>"+
                "<p> Simohamed Cainallah";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("yanissvaljean@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
