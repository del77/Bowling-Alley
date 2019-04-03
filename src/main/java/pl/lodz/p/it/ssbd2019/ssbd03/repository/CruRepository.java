package pl.lodz.p.it.ssbd2019.ssbd03.repository;

import java.util.List;
import java.util.Optional;

public interface CruRepository<T, ID> {

    void create(T entity);

    void edit(T entity);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    List<T> findAll();
}