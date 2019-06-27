package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.CommentDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.CommentEditDto;

public interface CommentService {
    /**
     * Pobiera komentarz po id
     *
     * @param commentId id komentarza
     * @param login     login użytkownika
     * @throws SsbdApplicationException
     */
    CommentDto getActiveCommentForUserById(Long commentId, String login) throws SsbdApplicationException;

    /**
     * Edytuje komentarz
     *
     * @param commentDto Obiekt zawierający komentarz
     * @throws SsbdApplicationException wyjatek aplikacyjny
     */
    void addComment(Long reservationId, CommentDto commentDto, String userLogin) throws SsbdApplicationException;

    /**
     * Dodaje komentarz
     *
     * @param comment Obiekt zawierający zaktualizowane dane
     * @param login   login użytkownika
     * @throws SsbdApplicationException
     */
    void editComment(CommentEditDto comment, String login) throws SsbdApplicationException;
}
