package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.ResetPasswordTokenRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.MessageNotSentException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.ResetPasswordException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.TokenUtils;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.ClassicMessage;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.mail.EmailMessenger;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import java.sql.Timestamp;

@Stateless
@TransactionAttribute
public class ResetPasswordServiceImpl implements ResetPasswordService {
    @EJB(beanName = "MOKUserRepository")
    UserAccountRepositoryLocal userAccountRepositoryLocal;

    @EJB(beanName = "MOKResetPasswordTokenRepository")
    ResetPasswordTokenRepositoryLocal resetPasswordTokenRepositoryLocal;

    @Inject
    EmailMessenger emailMessenger;

    @Override
    @PermitAll
    public void requestResetPassword(String email) throws ResetPasswordException {
        try {
            UserAccount userAccount = getUserByEmail(email);
            String token = TokenUtils.generate();
            Timestamp validity = TokenUtils.getValidity(24);

            ResetPasswordToken resetPasswordToken = ResetPasswordToken
                    .builder()
                    .token(token)
                    .userAccount(userAccount)
                    .validity(validity)
                    .build();

            resetPasswordTokenRepositoryLocal.create(resetPasswordToken);
            sendEmailWithToken(email, token);
        } catch (Exception e) {
            throw new ResetPasswordException(e.getMessage());
        }
    }

    @Override
    @PermitAll
    public void resetPassword(String token, String newPassword) throws ResetPasswordException {
        try {
            ResetPasswordToken resetPasswordToken = getToken(token);
            UserAccount userAccount = resetPasswordToken.getUserAccount();
            String newPasswordHash = SHA256Provider.encode(newPassword);

            if (!TokenUtils.isValid(resetPasswordToken.getValidity())) {
                throw new ResetPasswordException("Invalid token.");
            }

            userAccount.setPassword(newPasswordHash);
            resetPasswordToken.setValidity(new Timestamp(System.currentTimeMillis() - 1));
            userAccountRepositoryLocal.edit(userAccount);
        } catch (Exception e) {
            throw new ResetPasswordException(e.getMessage());
        }
    }

    /**
     * Pobiera użytkownika o zadanym adresie email
     *
     * @param email Adres email powiązany z kontem
     * @return konto użytkownika
     * @throws ResetPasswordException Wyjątek
     */
    private UserAccount getUserByEmail(String email) throws ResetPasswordException {
        try {
            return userAccountRepositoryLocal.findByEmail(email).orElseThrow(ResetPasswordException::new);
        } catch (Exception e) {
            throw new ResetPasswordException("Invalid email.");
        }
    }

    /**
     * Pobiera token dla zadanego ciągu
     *
     * @param token Unikalny token
     * @return token
     * @throws ResetPasswordException Wyjątek
     */
    private ResetPasswordToken getToken(String token) throws ResetPasswordException {
        try {
            return resetPasswordTokenRepositoryLocal.findByToken(token).orElseThrow(ResetPasswordException::new);
        } catch (Exception e) {
            throw new ResetPasswordException("Invalid token.");
        }
    }

    /**
     * Wysyła email z tokenem do użytkownika
     *
     * @param email Adres email użytkownika
     * @param token Token
     */
    private void sendEmailWithToken(String email, String token) throws MessageNotSentException {
        ClassicMessage message = ClassicMessage
                .builder()
                .from("ssbd201903@gmail.com")
                .to(email)
                .subject("Kregielnia - przypomnienie hasla")
                .body(token)
                .build();

        emailMessenger.sendMessage(message);
    }
}
