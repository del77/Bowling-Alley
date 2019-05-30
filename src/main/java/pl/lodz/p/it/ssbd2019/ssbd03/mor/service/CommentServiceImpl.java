package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful
@DenyAll
public class CommentServiceImpl implements CommentService {
    @Override
    @RolesAllowed(MorRoles.EDIT_COMMENT_FOR_RESERVATION)
    public void editComment(Comment comment) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed(MorRoles.DISABLE_COMMENT)
    public void disableComment(Long id) {
        throw new UnsupportedOperationException();
    }
}
