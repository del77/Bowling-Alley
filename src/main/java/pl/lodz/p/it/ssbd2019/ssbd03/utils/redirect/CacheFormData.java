package pl.lodz.p.it.ssbd2019.ssbd03.utils.redirect;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Optional;

@SessionScoped
public class CacheFormData implements Serializable {
    private HashMap<Long, FormData> cache = new HashMap<>();

    void save(Long key, FormData element) {
        synchronized (this) {
            cache.put(key, element);
        }
    }

    public Optional<FormData> get(Long key) {
        synchronized (this) {
            return Optional.of(cache.get(key));
        }
    }
}
