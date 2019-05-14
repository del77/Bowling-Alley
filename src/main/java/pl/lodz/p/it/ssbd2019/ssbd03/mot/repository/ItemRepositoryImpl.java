package pl.lodz.p.it.ssbd2019.ssbd03.mot.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Item;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@DenyAll
public class ItemRepositoryImpl extends AbstractCruRepository<Item, Long> implements ItemRepositoryLocal {

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
    @RolesAllowed({"EditBallsCount", "EditShoesCount"})
    public Item edit(Item item) {
        return super.edit(item);
    }
}
