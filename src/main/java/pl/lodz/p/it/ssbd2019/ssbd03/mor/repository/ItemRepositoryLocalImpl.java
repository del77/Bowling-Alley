package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Item;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.EntityRetrievalException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Stateless(name = "MORItemRepository")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class ItemRepositoryLocalImpl extends AbstractCruRepository<Item, Long> implements ItemRepositoryLocal {
    
    @PersistenceContext(unitName = "ssbd03morPU")
    private EntityManager entityManager;
    
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
    
    @Override
    protected Class<Item> getTypeParameterClass() {
        return Item.class;
    }
    
    @Override
    @RolesAllowed({MorRoles.EDIT_RESERVATION_FOR_USER, MorRoles.EDIT_OWN_RESERVATION})
    public Optional<Item> findBySize(int size) throws EntityRetrievalException {
        try {
            TypedQuery<Item> namedQuery = this.createNamedQuery("Item.findBySize");
            namedQuery.setParameter("size", size);
            return Optional.of(namedQuery.getSingleResult());
        } catch (Exception e) {
            throw new EntityRetrievalException(e);
        }
    }
}
