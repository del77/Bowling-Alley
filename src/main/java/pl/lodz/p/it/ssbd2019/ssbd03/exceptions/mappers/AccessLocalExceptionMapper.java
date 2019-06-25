package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.mappers;

import javax.ejb.AccessLocalException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccessLocalExceptionMapper implements ExceptionMapper<AccessLocalException> {

    @Override
    public Response toResponse(AccessLocalException e) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
