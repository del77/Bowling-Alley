package pl.lodz.p.it.ssbd2019.ssbd03.accountsmodule.web.register;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Optional;

@ApplicationScoped
class CacheDto {
    private HashMap<Long, FormData> cache = new HashMap<>();

    void save(Long key, FormData element) {
        synchronized (this) {
            cache.put(key, element);
        }
    }

    Optional<FormData> get(Long key) {
        synchronized (this) {
            return Optional.of(cache.get(key));
        }
    }
}
