package pl.lodz.p.it.ssbd2019.ssbd03.utils;

import lombok.Data;

@Data
public class Tuple<T, K> {
    private final T first;
    private final K second;

    private Tuple(final T t, final K k) {
        this.first = t;
        this.second = k;
    }

    public static <T, K> Tuple<T, K> of(final T t, final  K k) {
        return new Tuple<>(t, k);
    }
}
