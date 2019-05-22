package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.mappers;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.StateConflictedException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundMapper implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(NotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
