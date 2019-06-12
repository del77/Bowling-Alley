package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

public interface CommentService {
    /**
     * Edytuje komentarz
     *
     * @param comment Obiekt zawierający zaktualizowane dane
     */
    void editComment(Comment comment);

    /**
     * Blokuje wybrany komentarz
     * @param id identyfikator komentarza
     */
    void disableComment(Long id);
}
