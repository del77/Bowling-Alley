package pl.lodz.p.it.ssbd2019.ssbd03.mok.web.mappers;

import org.modelmapper.ModelMapper;

import javax.ws.rs.Produces;

public class Mapper {

    @Produces
    public ModelMapper modelMapperProvider() {
        return new ModelMapper();
    }
}
