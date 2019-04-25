package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.service;

import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccessLevelRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository.AccountRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.EntityRetrievalException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

@Stateless
@Transactional
public class AccessLevelServiceImpl implements AccessLevelService {

    @EJB(beanName = "MOKAccessLevelRepository")
    AccessLevelRepositoryLocal accessLevelRepositoryLocal;

    @Override
    public AccessLevel getAccessLevelByName(String name) throws EntityRetrievalException {
        try {
            return accessLevelRepositoryLocal.findByName(name).orElseThrow(
                    () -> new EntityRetrievalException("There is no AccessLevel with given name"));
        } catch (Exception e) {
            throw new EntityRetrievalException("Could not retrieve AccessLevel by name", e);
        }
    }
}
