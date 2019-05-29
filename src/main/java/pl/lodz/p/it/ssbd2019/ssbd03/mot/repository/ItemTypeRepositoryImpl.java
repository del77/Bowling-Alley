package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.ItemType;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class ItemTypeRepositoryImpl extends AbstractCruRepository<ItemType, Long> implements ItemTypeRepositoryLocal {
    @PersistenceContext(unitName = "ssbd03motPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<ItemType> getTypeParameterClass() {
        return ItemType.class;
    }

    @Override
    @RolesAllowed({MotRoles.EDIT_BALLS_COUNT, MotRoles.EDIT_SHOES_COUNT})
    public Optional<ItemType> findByType(String type) {
        throw new UnsupportedOperationException();
    }
}
