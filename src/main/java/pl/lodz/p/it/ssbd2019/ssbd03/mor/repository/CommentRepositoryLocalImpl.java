package pl.lodz.p.it.ssbd2019.ssbd03.mor.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.CommentOptimisticLockException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DatabaseConstraintViolationException;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.AbstractCruRepository;
import pl.lodz.p.it.ssbd2019.ssbd03.utils.roles.MorRoles;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

@Stateless(name = "MORCommentRepository")
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@DenyAll
public class CommentRepositoryLocalImpl extends AbstractCruRepository<Comment, Long> implements CommentRepositoryLocal {

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
    @RolesAllowed({MorRoles.EDIT_COMMENT_FOR_OWN_RESERVATION, MorRoles.DISABLE_COMMENT})
    public void edit(Comment comment) throws DataAccessException {
        try {
            super.edit(comment);
        } catch (OptimisticLockException e) {
            throw new CommentOptimisticLockException(e);
        } catch (ConstraintViolationException e) {
            throw new DatabaseConstraintViolationException(e);
        }
    }
}
