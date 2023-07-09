package com.candyquizz.registration;

import com.candyquizz.event.RegistrationCompleteEvent;
import com.candyquizz.event.listener.RegistrationCompleteEventListener;
import com.candyquizz.registration.password.PasswordResetRequest;
import com.candyquizz.registration.token.VerificationToken;
import com.candyquizz.registration.token.VerificationTokenRepository;
import com.candyquizz.user.User;
import com.candyquizz.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;
    private final RegistrationCompleteEventListener eventListener;


    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request){
        User user = userService.registerUser(registrationRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Bravo ! check your e-mail pour finaliser votre inscription";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken theToken = tokenRepository.findByToken(token);
        if (theToken.getUser().isEnabled()){
            return "Ce compte a déjà été vérifié, merci de se connecter.";
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            return "Email vérifié avec succès, vous pouvez maintenant se connecter";
        }
        return "Token de vérification invalid";
    }


    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(@RequestBody  PasswordResetRequest passwordResetRequest,
                                       final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        Optional<User> user = userService.findByEmail(passwordResetRequest.getEmail());
        String passwordResetUrl = "";
        if (user.isPresent()) {
            String passwordResetToken = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
            passwordResetUrl = passwordResetEmailLink(user.get(), applicationUrl(request), passwordResetToken);
        }
        return passwordResetUrl;
    }

    private String passwordResetEmailLink(User user, String applicationUrl, String passwordResetToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl+"/register/reset-password?token="+passwordResetToken;
        eventListener.sentPasswordResetVerificationEmail(url);
        log.info("Cliquez ici pour renouvellez votre mot de passe : {}", url);
        return url;
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordResetRequest passwordResetRequest,
                                @RequestParam("token") String passwordResetToken) {
        String tokenValidationResult = userService.validatePasswordResetToken(passwordResetToken);
        if (!tokenValidationResult.equalsIgnoreCase("valid")) {
            return "Token invalid";
        }
        User user = userService.findUserByPasswordToken(passwordResetToken);
        if (user != null) {
            userService.resetUserPassword(user, passwordResetRequest.getNewPassword());
            return "Le mot de passe a été reinsialisé avec succès";
        }
        return "Token pour réinsialiser le mot de passe est invalide";
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
