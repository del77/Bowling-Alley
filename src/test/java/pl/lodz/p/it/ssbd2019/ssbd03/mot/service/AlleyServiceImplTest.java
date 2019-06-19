package pl.lodz.p.it.ssbd2019.ssbd03.mot.service;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.p.it.ssbd2019.ssbd03.entities.Alley;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.repository.AlleyRepositoryLocal;
import pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto.AlleyDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AlleyServiceImplTest {
    @Mock
    private AlleyRepositoryLocal alleyRepositoryLocal;
    
    @InjectMocks
    private AlleyServiceImpl alleyService;
    
    @Test
    public void getAllAlleysTest() throws SsbdApplicationException {
        List<Alley> alleys = Arrays.asList(
                Alley.builder().id(0L).number(1).active(true).maxScore(10).build(),
                Alley.builder().id(1L).number(2).active(false).maxScore(100).build()
        );
        when(alleyRepositoryLocal.findAll()).thenReturn(alleys);
        List<AlleyDto> dtos = alleyService.getAllAlleys();
        assertEquals(2, dtos.size());
        assertEquals(10, alleys.get(0).getMaxScore());
        assertEquals(100, alleys.get(1).getMaxScore());
        assertEquals(1, alleys.get(0).getNumber());
        assertEquals(2, alleys.get(1).getNumber());
        assertTrue(alleys.get(0).isActive());
        assertFalse(alleys.get(1).isActive());
    }
}