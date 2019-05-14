package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;

import javax.annotation.security.RolesAllowed;

public class CommentServiceImpl implements CommentService {
    @Override
    @RolesAllowed("EditCommentForReservation")
    public void editComment(Comment comment) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RolesAllowed("DisableComment")
    public void disableComment(Long id) {
        throw new UnsupportedOperationException();
    }
}
