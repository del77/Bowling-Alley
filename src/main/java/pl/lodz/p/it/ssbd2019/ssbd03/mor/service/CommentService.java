package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.Comment;

public interface CommentService {
    /**
     * Edytuje komentarz
     *
     * @param comment Obiekt zawierający zaktualizowane dane
     */
    void editComment(Comment comment);


}
