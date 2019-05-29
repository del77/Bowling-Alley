package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class CommentRepositoryImpl extends AbstractCruRepository<Comment, Long> implements CommentRepositoryLocal {

    @PersistenceContext(unitName = "ssbd03morPU")
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected Class<Comment> getTypeParameterClass() {
        return Comment.class;
    }

    @Override
    @RolesAllowed({MorRoles.EDIT_COMMENT_FOR_RESERVATION, MorRoles.DISABLE_COMMENT})
    public void edit(Comment comment) throws DataAccessException {
        super.edit(comment);
    }
}
