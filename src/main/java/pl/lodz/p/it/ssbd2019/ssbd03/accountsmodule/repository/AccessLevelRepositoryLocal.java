package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.repository;

import pl.lodz.p.it.ssbd2019.ssbd03.entities.AccessLevel;
import pl.lodz.p.it.ssbd2019.ssbd03.repository.CruRepository;

import javax.ejb.Local;
import java.util.Optional;

@Local
public interface AccessLevelRepositoryLocal extends CruRepository<AccessLevel, Long> {
    /**
     * Metoda służy do pozyskiwania ancji poziomu dostępu na podstawie jej nazwy w bazie danych.
     * Przydatna przy tworzeniu użytkownika w procesie rejestracji.
     * @param name Nazwa poziomu dostępu
     * @return Poziom dostępu
     */
    Optional<AccessLevel> findByName(String name);
}
