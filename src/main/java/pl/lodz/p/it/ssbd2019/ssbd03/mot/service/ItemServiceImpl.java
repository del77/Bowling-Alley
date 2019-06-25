package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Item;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.notfound.NotFoundException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.ItemRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ItemDto;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers.Mapper;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.InterceptorTracker;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.TransactionTracker;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.List;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(InterceptorTracker.class)
@Stateful
@DenyAll
public class ItemServiceImpl extends TransactionTracker implements ItemService {

    @EJB(beanName = "MOTItemRepository")
    private ItemRepositoryLocal itemRepositoryLocal;

    private List<Item> items;

    @Override
    @RolesAllowed({MotRoles.EDIT_BALLS_COUNT, MotRoles.EDIT_SHOES_COUNT})
    public void updateItems(List<ItemDto> updatedItems) throws SsbdApplicationException {
        for(ItemDto updatedItem : updatedItems) {
            Item item = items.stream()
                    .filter(x -> x.getSize() == updatedItem.getSize())
                    .findFirst()
                    .orElse(null);

            if(item == null) {
                throw new NotFoundException("There is no item with sizes: " + updatedItem.getSize());
            }

            item.setCount(updatedItem.getCount());
            itemRepositoryLocal.edit(item);
        }
    }

    @Override
    @RolesAllowed({MotRoles.EDIT_BALLS_COUNT, MotRoles.EDIT_SHOES_COUNT})
    public List<ItemDto> getItemsBySpecifiedItemType(String itemType) {
        items = itemRepositoryLocal.findAllByItemType(itemType);

        return Mapper.mapAll(items, ItemDto.class);
    }
}
