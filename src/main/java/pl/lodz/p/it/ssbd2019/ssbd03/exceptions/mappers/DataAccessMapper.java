package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.mappers;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;
import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.entity.DataAccessException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DataAccessMapper implements ExceptionMapper<DataAccessException> {
    @Override
    public Response toResponse(DataAccessException exception) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
    }
}
