package pl.lodz.p.it.ssbd2019.ssbd03.entities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAccountTest {
    
    @Mock
    private Validator validator;
    
    @Test
    public void setPhoneFormatWithSpacesTest() {
        UserAccount user = UserAccount.builder()
            .phone("123 123 123")
            .build();
    
        Set<ConstraintViolation<UserAccount>> violations =
                user.getPhone().matches("^[0-9]*$") ?
                        new HashSet<>() :
                        Collections.singleton(null);
    
        when(validator.validate(any(UserAccount.class))).thenReturn(violations);
    
        assertFalse(validator.validate(user).isEmpty()); // there should be errors
    }
    
    @Test
    public void setPhoneFormatWithHyphensTest() {
        UserAccount user = UserAccount.builder()
                .phone("123-123-123")
                .build();
        
        Set<ConstraintViolation<UserAccount>> violations =
                user.getPhone().matches("^[0-9]*$") ?
                        new HashSet<>() :
                        Collections.singleton(null);
        
        when(validator.validate(any(UserAccount.class))).thenReturn(violations);
        
        assertFalse(validator.validate(user).isEmpty()); // there should be errors
    }
    
    @Test
    public void setPhoneWithLettersTest() {
        UserAccount user = UserAccount.builder()
                .phone("123abcxyz")
                .build();
        
        Set<ConstraintViolation<UserAccount>> violations =
                user.getPhone().matches("^[0-9]*$") ?
                        new HashSet<>() :
                        Collections.singleton(null);
        
        when(validator.validate(any(UserAccount.class))).thenReturn(violations);
        
        assertFalse(validator.validate(user).isEmpty()); // there should be errors
    }
    
    
    @Test
    public void setPhoneCorrectFormatTest() {
        UserAccount user = UserAccount.builder()
                .phone("123456789")
                .build();
    
        Set<ConstraintViolation<UserAccount>> violations =
                user.getPhone().matches("^[0-9]*$") ?
                        new HashSet<>() :
                        Collections.singleton(null);
    
        when(validator.validate(any(UserAccount.class))).thenReturn(violations);
    
        assertTrue(validator.validate(user).isEmpty()); // there shouldn't be any errors
    }
}