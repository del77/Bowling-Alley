package pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers;

import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    private static ModelMapper modelMapper = new ModelMapper();

    private Mapper() {}

    /**
     * Mapuje jeden obiekt na drugi
     * @param source obiekt wejściowy
     * @param destinationClass klasa obiektu wyjściowego
     * @return zmapowany obiekt
     */
    public static <D, T> D map(final T source, Class<D> destinationClass) {
        return modelMapper.map(source, destinationClass);
    }

    /**
     * Mapuje jedną listę do drugiej
     * @param sourceList lista obiektów wejściowych
     * @param destinationClass klasa obiektów w liście wyjściowej
     * @return lista zmapowanych obiektów
     */
    public static <D, T> List<D> mapAll(final Collection<T> sourceList, Class<D> destinationClass) {
        return sourceList.stream()
                .map(entity -> map(entity, destinationClass))
                .collect(Collectors.toList());
    }

    /**
     * Mapuje jeden obiekt na drugi.
     * @param source obiekt wejściowy.
     * @param destination obiekt wyjściowy.
     */
    public static <S, D> D map(final S source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }
}
