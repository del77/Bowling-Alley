package pl.lodz.p.it.ssbd2019.ssbd03.exceptions.mappers;

import pl.lodz.p.it.ssbd2019.ssbd03.exceptions.conflict.StateConflictedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StateConflictedMapper implements ExceptionMapper<StateConflictedException> {
    @Override
    public Response toResponse(StateConflictedException exception) {
        return Response.status(Response.Status.CONFLICT).build();
    }
}
