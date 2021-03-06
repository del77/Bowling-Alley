package pl.lodz.p.it.ssbd2019.ssbd03.utils.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.ReservationItem;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Score;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationFullDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationFullItemDto;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.ScoreDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Mapper {
    private static ModelMapper modelMapper = new ModelMapper();

    static {
        //ScoreDto
        TypeMap<Score, ScoreDto> scoreTypeMap = modelMapper.createTypeMap(Score.class, ScoreDto.class);
        scoreTypeMap.addMappings(mapper -> {
            mapper.map(src -> src.getReservation().getStartDate(), ScoreDto::setDate);
            mapper.map(src -> src.getUserAccount().getLogin(), ScoreDto::setLogin);
        });

        //ReservationFullDto
        TypeMap<Reservation, ReservationFullDto> reservationFullTypeMap = modelMapper.createTypeMap(Reservation.class, ReservationFullDto.class);
        reservationFullTypeMap.addMappings(mapper -> {
            mapper.map(src -> src.getUserAccount().getLogin(), ReservationFullDto::setUserLogin);
            mapper.map(Reservation::getComments, ReservationFullDto::setComments);
        });

        //ReservationFullItemDto
        TypeMap<ReservationItem, ReservationFullItemDto> reservationItemTypeMap = modelMapper.createTypeMap(ReservationItem.class, ReservationFullItemDto.class);
        reservationItemTypeMap.addMappings(mapper -> {
            mapper.map(src -> src.getItem().getSize(), ReservationFullItemDto::setSize);
            mapper.map(src -> src.getItem().getItemType().getName(), ReservationFullItemDto::setName);
        });
    }

    /**
     * Mapuje jeden obiekt na drugi
     *
     * @param source           obiekt wejściowy
     * @param destinationClass klasa obiektu wyjściowego
     * @return zmapowany obiekt
     */
    public static <D, T> D map(final T source, Class<D> destinationClass) {
        return modelMapper.map(source, destinationClass);
    }

    /**
     * Mapuje jedną listę do drugiej
     *
     * @param sourceList       lista obiektów wejściowych
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
     *
     * @param source      obiekt wejściowy.
     * @param destination obiekt wyjściowy.
     */
    public static <S, D> D map(final S source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }
}
