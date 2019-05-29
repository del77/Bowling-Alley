package pl.lodz.p.it.ssbd2019.ssbd03.web.jacc;


import java.util.List;
import java.util.stream.Collectors;

import pl.lodz.p.it.ssbd2019.ssbd03.mok.repository.AuthenticationViewEntityRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AuthenticationViewEntity;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.SHA256Provider;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.logging.Logger;


/**
 * Klasa wykonująca walidację danych podanych w formularzu logowania
 */
@Singleton
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AuthenticationViewIdentityStore implements IdentityStore {

    private static final Logger logger = Logger.getLogger(AuthenticationViewIdentityStore.class.getName());

    @EJB(beanName = "MOKAuthenticationViewEntityRepositoryLocalImpl")
    AuthenticationViewEntityRepositoryLocal authenticationViewEntityRepositoryLocal;

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        try {
            List<AuthenticationViewEntity> entities = authenticationViewEntityRepositoryLocal.findAllWithLogin(credential.getCaller());
            if (entities.isEmpty()) {
                return CredentialValidationResult.NOT_VALIDATED_RESULT;
            } else {
                String inputPasswordHashed =
                        SHA256Provider.encode(credential.getPasswordAsString());
                if (inputPasswordHashed.equalsIgnoreCase(entities.get(0).getPassword())) {
                    return new CredentialValidationResult(
                            credential.getCaller(),
                            entities.stream()
                                    .map(AuthenticationViewEntity::getAccessLevelName)
                                    .collect(Collectors.toSet()));
                } else {
                    return CredentialValidationResult.NOT_VALIDATED_RESULT;
                }
            }
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
        return CredentialValidationResult.INVALID_RESULT;
    }
}
