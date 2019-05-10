package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.Timestamp;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTest {
    
    private Validator validator;
    
    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    public void setEndDateBeforeStartDateTest() {
        Reservation reservation = Reservation.builder()
                .active(true)
                .alley(new Alley())
                .id(1L)
                .playersCount(1)
                .userAccount(new UserAccount())
                .version(0)
                .build();
        reservation.setStartDate(new Timestamp(System.currentTimeMillis() + 10000));
        reservation.setEndDate(new Timestamp(System.currentTimeMillis() + 5000));
        Set<ConstraintViolation<Reservation>> violations = validator.validate(reservation);
        assertFalse(violations.isEmpty()); // there should be errors
    }
    
    @Test
    public void setEndDateAfterStartDateTest() {
        Reservation reservation = Reservation.builder()
                .active(true)
                .alley(new Alley())
                .id(1L)
                .playersCount(1)
                .userAccount(new UserAccount())
                .version(0)
                .build();
        reservation.setStartDate(new Timestamp(System.currentTimeMillis() + 10000));
        reservation.setEndDate(new Timestamp(System.currentTimeMillis() + 15000));
        Set<ConstraintViolation<Reservation>> violations = validator.validate(reservation);
        assertTrue(violations.isEmpty()); // there shouldn't be any errors
    }
}