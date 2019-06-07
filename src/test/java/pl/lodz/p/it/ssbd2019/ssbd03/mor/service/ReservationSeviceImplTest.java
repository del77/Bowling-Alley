package pl.lodz.p.it.ssbd2019.ssbd03.mor.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Reservation;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.repository.ReservationRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto.ReservationDto;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationSeviceImplTest {

    @Mock
    private ReservationRepositoryLocal repositoryLocal;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    public void getAllReservationsTest() throws SsbdApplicationException {
        Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());
        Timestamp timestamp2 = new Timestamp(System.currentTimeMillis() + 60 * 2000);
        Timestamp timestamp3 = new Timestamp(System.currentTimeMillis() + 60 * 3000);

        Alley alley = mock(Alley.class);
        List<Reservation> reservations = Arrays.asList(
                Reservation.builder()
                        .startDate(timestamp1)
                        .endDate(timestamp2)
                        .playersCount(21)
                        .active(true)
                        .alley(alley)
                        .build(),
                Reservation.builder()
                        .startDate(timestamp2)
                        .endDate(timestamp3)
                        .playersCount(2)
                        .active(false)
                        .alley(alley)
                        .build()
        );

        when(alley.getNumber()).thenReturn(2);
        when(repositoryLocal.findReservationsForUser(12L)).thenReturn(reservations);

        List<ReservationDto> dtos = reservationService.getReservationsForUser(12L);
        assertEquals(2, dtos.size());
        assertEquals(timestamp1, dtos.get(0).getStartDate());
        assertEquals(timestamp2, dtos.get(1).getStartDate());
        assertEquals(timestamp2, dtos.get(0).getEndDate());
        assertEquals(timestamp3, dtos.get(1).getEndDate());
        assertTrue(dtos.get(0).isActive());
        assertFalse(dtos.get(1).isActive());
    }
}
