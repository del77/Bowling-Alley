package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Item;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MotRoles;

import javax.annotation.security.RolesAllowed;
import java.util.List;

public class ItemServiceImpl implements ItemService {
    @Override
    @RolesAllowed({MotRoles.EDIT_BALLS_COUNT, MotRoles.EDIT_SHOES_COUNT})
    public void updateItemCount(Long id, int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({MotRoles.EDIT_BALLS_COUNT, MotRoles.EDIT_SHOES_COUNT})
    public List<Item> getItemsBySpecifiedItemType(String itemType) {
        throw new UnsupportedOperationException();
    }
}
