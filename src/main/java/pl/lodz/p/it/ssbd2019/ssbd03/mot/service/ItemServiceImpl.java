package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Item;

import javax.annotation.security.RolesAllowed;
import java.util.List;

public class ItemServiceImpl implements ItemService {
    @Override
    @RolesAllowed({"EditBallsCount", "EditShoesCount"})
    public void updateItemCount(Long id, int count) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed({"EditBallsCount", "EditShoesCount"})
    public List<Item> getItemsBySpecifiedItemType(String itemType) {
        throw new UnsupportedOperationException();
    }
}
