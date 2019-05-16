package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.ResetPasswordTokenRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.UserAccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.UserAccount;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.ChangePasswordException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.ResetPasswordException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.TextParsingException;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.TokenUtils;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.ClassicMessage;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.messaging.mail.EmailMessenger;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResetPasswordServiceImplTest {

    @Mock
    private UserAccountRepositoryLocal userAccountRepositoryLocal;

    @Mock
    private ResetPasswordTokenRepositoryLocal resetPasswordTokenRepositoryLocal;

    @Mock
    EmailMessenger emailMessenger;

    @InjectMocks
    private ResetPasswordServiceImpl resetPasswordService;

    @Test
    public void shouldRequestResetPassword() {
        String email = "test@example.com";

        UserAccount user = UserAccount
                .builder()
                .email(email)
                .build();

        when(userAccountRepositoryLocal.findByEmail(anyString())).thenReturn(Optional.of(user));

        try {
            ResetPasswordToken resetPasswordToken = resetPasswordService.requestResetPassword(email);
            Assertions.assertEquals(user, resetPasswordToken.getUserAccount());
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void shouldThrowWhenWrongEmail() {
        String email = "test@example.com";

        when(userAccountRepositoryLocal.findByEmail(anyString())).thenReturn(Optional.empty());

        try {
            Assertions.assertThrows(ResetPasswordException.class, () ->
                    resetPasswordService.requestResetPassword(email));
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void shouldResetPassword() throws TextParsingException, NoSuchAlgorithmException {
        String token = TokenUtils.generate();
        Timestamp validity = TokenUtils.getValidity(24);
        String newPassword = "N3WP4A5W0RD";
        String newPasswordHash = SHA256Provider.encode(newPassword);

        ResetPasswordToken resetPasswordToken = ResetPasswordToken
                .builder()
                .userAccount(UserAccount.builder().build())
                .validity(validity)
                .build();

        when(resetPasswordTokenRepositoryLocal.findByToken(anyString())).thenReturn(Optional.of(resetPasswordToken));

        try {
            UserAccount userAccount = resetPasswordService.resetPassword(token, newPassword);
            Assertions.assertEquals(newPasswordHash, userAccount.getPassword());
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void shouldThrowWhenWrongToken() {
        String token = TokenUtils.generate();
        String newPassword = "N3WP4A5W0RD";

        when(resetPasswordTokenRepositoryLocal.findByToken(anyString())).thenReturn(Optional.empty());

        try {
            Assertions.assertThrows(ResetPasswordException.class, () ->
                    resetPasswordService.resetPassword(token, newPassword));
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void shouldThrowWhenTokenExpired() throws TextParsingException, NoSuchAlgorithmException {
        String token = TokenUtils.generate();
        Timestamp validity = TokenUtils.getValidity(-5);
        String newPassword = "N3WP4A5W0RD";

        ResetPasswordToken resetPasswordToken = ResetPasswordToken
                .builder()
                .userAccount(UserAccount.builder().build())
                .validity(validity)
                .build();

        when(resetPasswordTokenRepositoryLocal.findByToken(anyString())).thenReturn(Optional.of(resetPasswordToken));

        try {
            Assertions.assertThrows(ResetPasswordException.class, () ->
                    resetPasswordService.resetPassword(token, newPassword));
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }
}
