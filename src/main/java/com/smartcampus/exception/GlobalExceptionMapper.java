package com.smartcampus.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Global safety net: catches ALL uncaught Throwable types.
 *
 * Security rationale: Exposing raw Java stack traces to API consumers is a
 * significant security risk. A stack trace can reveal:
 *   - Internal package structure and class names (facilitates targeted attacks)
 *   - Third-party library names and versions (enables known CVE exploitation)
 *   - Internal file system paths (aids server enumeration)
 *   - Application logic and control flow (helps craft injection payloads)
 *
 * This mapper logs the full detail internally (server-side only) and returns
 * a clean, generic 500 response to the client — no internal info leaked.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        // Re-throw specific exceptions so their own mappers handle them
    if (exception instanceof RoomNotEmptyException) {
        return new RoomNotEmptyExceptionMapper().toResponse((RoomNotEmptyException) exception);
    }
    if (exception instanceof LinkedResourceNotFoundException) {
        return new LinkedResourceNotFoundExceptionMapper().toResponse((LinkedResourceNotFoundException) exception);
    }
    if (exception instanceof SensorUnavailableException) {
        return new SensorUnavailableExceptionMapper().toResponse((SensorUnavailableException) exception);
    }
        
        
        
        
        
        
        
        // Log full detail SERVER-SIDE only — never sent to the client
        LOGGER.log(Level.SEVERE, "Unhandled exception caught by global mapper: " + exception.getMessage(), exception);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                        "error", "INTERNAL_SERVER_ERROR",
                        "status", 500,
                        "message", "An unexpected error occurred. Please contact the system administrator.",
                        "hint", "No internal details are disclosed for security reasons."
                ))
                .build();
    }
}
