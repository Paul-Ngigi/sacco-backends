package org.sacco.backend.utils;

import io.vertx.core.json.JsonObject;
import javax.ws.rs.core.Response;
import org.jboss.logging.Logger;

public class Respond {

    private static final Logger logger =
        Logger.getLogger(Respond.class);

    public static Response getSuccessResponse(final String route,
    final JsonObject response) {

            JsonObject res = new JsonObject()
                .put("status", 200)
                .put("message", "success")
                .put("payload", response);

            logger.info(route + " <- ()");
            return Response.ok(res).build();
        }

    public static Response getErrorResponse(final String route,
        final String message) {

        JsonObject res = new JsonObject()
            .put("status", 502)
            .put("message", "error")
            .put("payload", message);

        logger.info(route + " <- ()");
        return Response.ok(res).build();
    }

    public static Response getErrorResponse(final String route,
        final Exception e) {

        JsonObject res = new JsonObject()
            .put("status", 500)
            .put("message", "error")
            .put("payload", e.getLocalizedMessage());

        logger.info(route + " <- ()");
        return Response.ok(res).build();
    }

    public static Response getErrorResponse(final String route,
    final Throwable e) {

        JsonObject res = new JsonObject()
            .put("status", 500)
            .put("message", "error")
            .put("payload", e.getCause());

        logger.info(route + " <- ()");
        return Response.ok(res).build();
}

}
