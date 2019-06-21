package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.CommentDto;

public interface CommentService {
    /**
     * Edytuje komentarz
     *
     * @param commentDto Obiekt zawierający komentarz
     * @throws SsbdApplicationException
     */
    void addComment(Long reservationId, CommentDto commentDto) throws SsbdApplicationException;

    /**
     * Dodaje komentarz
     *
     * @param comment Obiekt zawierający zaktualizowane dane
     */
    void editComment(Comment comment);
}
