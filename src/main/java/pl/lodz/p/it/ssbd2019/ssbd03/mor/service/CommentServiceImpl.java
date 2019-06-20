package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.InterceptorTracker;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.tracker.TransactionTracker;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateful
@Interceptors(InterceptorTracker.class)
@DenyAll
public class CommentServiceImpl extends TransactionTracker implements CommentService {
    @Override
    @RolesAllowed(MorRoles.EDIT_COMMENT_FOR_RESERVATION)
    public void editComment(Comment comment) {
        throw new UnsupportedOperationException();
    }


}
