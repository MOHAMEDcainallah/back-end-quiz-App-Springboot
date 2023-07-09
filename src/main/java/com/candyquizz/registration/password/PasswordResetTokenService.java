package com.candyquizz.registration.password;

import com.candyquizz.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(passwordToken, user);
        passwordResetTokenRepository.save(passwordResetToken);
    }
    public String validatePasswordResetToken(String theToken) {

            PasswordResetToken token = passwordResetTokenRepository.findByToken(theToken);
            if(token == null){
                return "Token pour réinsialiser le mot de passe invalid";
            }
            User user = token.getUser();
            Calendar calendar = Calendar.getInstance();
            if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
                return "Le lien a expiré, envoyez un nouveau mail";
            }
            return "valid";
        }

        public Optional<User> findUserByPasswordToken(String passwordToken) {
            return Optional.ofNullable(passwordResetTokenRepository.findByToken(passwordToken).getUser());
        }
}
