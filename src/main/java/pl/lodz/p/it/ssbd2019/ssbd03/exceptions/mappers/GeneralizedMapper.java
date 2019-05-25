package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.mappers;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.SsbdApplicationException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GeneralizedMapper implements ExceptionMapper<SsbdApplicationException> {
    @Override
    public Response toResponse(SsbdApplicationException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
