package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Item;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.*;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless(name = "MOTItemRepository")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class ItemRepositoryLocalImpl extends AbstractCruRepository<Item, Long> implements ItemRepositoryLocal {

    @PersistenceContext(unitName = "ssbd03motPU")
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
    @RolesAllowed({MotRoles.EDIT_BALLS_COUNT, MotRoles.EDIT_SHOES_COUNT})
    public void edit(Item item) throws DataAccessException {
        try {
            super.edit(item);
        } catch (OptimisticLockException e) {
            throw new ItemOptimisticLockException("Item has been updated before these changes were made", e);
        } catch (ConstraintViolationException e) {
            if(e.getMessage().contains("counts")) {
                throw new ItemCountConstraintViolationException("Given counts: " + item.getCount() + " is invalid.");
            }
            throw new DatabaseConstraintViolationException("Violated constraint during item editing", e);
        } catch (PersistenceException e) {
            throw new EntityUpdateException("Could not perform edit operation.", e);
        }
    }

    @Override
    @RolesAllowed({MotRoles.EDIT_BALLS_COUNT, MotRoles.EDIT_SHOES_COUNT})
    public List<Item> findAllByItemType(String itemType) {
        TypedQuery<Item> namedQuery = this.createNamedQuery("Item.findByItemType");
        namedQuery.setParameter("itemType", itemType);
        return namedQuery.getResultList();
    }
}
