package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ReservationTest {
    
    @Mock
    private Validator validator;
    
    @Test
    public void setEndDateBeforeStartDateTest() {
        Reservation reservation = Reservation.builder()
                .startDate(new Timestamp(System.currentTimeMillis() + 10000))
                .endDate(new Timestamp(System.currentTimeMillis() + 5000))
                .build();
        
        Set<ConstraintViolation<Reservation>> violations =
                reservation.getEndDate().after(reservation.getStartDate()) ?
                        new HashSet<>() :
                        Collections.singleton(null);
        
        when(validator.validate(any(Reservation.class))).thenReturn(violations);
        
        assertFalse(validator.validate(reservation).isEmpty()); // there should be errors
    }
    
    @Test
    public void setEndDateAfterStartDateTest() {
        Reservation reservation = Reservation.builder()
                .startDate(new Timestamp(System.currentTimeMillis() + 10000))
                .endDate(new Timestamp(System.currentTimeMillis() + 20000))
                .build();
    
        Set<ConstraintViolation<Reservation>> violations =
                reservation.getEndDate().after(reservation.getStartDate()) ?
                        new HashSet<>() :
                        Collections.singleton(null);
    
        when(validator.validate(any(Reservation.class))).thenReturn(violations);
        
        assertTrue(validator.validate(reservation).isEmpty()); // there shouldn't be any errors
    }
}