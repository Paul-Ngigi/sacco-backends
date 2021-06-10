package org.sacco.backend.views;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;
import org.sacco.backend.GreetingResource;
import org.sacco.backend.interfaces.UserInterface;
import org.sacco.backend.models.HashPassword;
import org.sacco.backend.models.Role;
import org.sacco.backend.models.Users;
import org.sacco.backend.utils.Respond;
import org.sacco.backend.utils.TokenUtils;

import io.vertx.core.json.JsonObject;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class Auth {

    private static final Logger logger =
        Logger.getLogger(Auth.class);

    private final UserInterface userInterface;

    public Auth(final UserInterface uInterface) {
        this.userInterface = uInterface;
    }

    @POST
    @Path("/signUp")
    public Response signUp(@Valid JsonObject xusr) {
        logger.info("signUp -> ()");
        Users user = new Users(xusr.getString("firstName"),
            xusr.getString("middleName"), xusr.getString("lastName"),
                xusr.getString("email").toLowerCase(),
                xusr.getString("password"));

        Set<Role> roles = new HashSet<>();
        roles.add(Role.CLIENT);
        user.setRole(Role.CLIENT.toString());
        try {
            String token = TokenUtils.generateToken(user.getEmail(),
                roles, Long.valueOf(84600), "quarkus");
            user.setToken(token);
            this.userInterface.save(user);
            return Respond.getSuccessResponse("login",
                JsonObject.mapFrom(user));
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return Respond.getErrorResponse("sign up", e);
        }

    }

    @POST
    @Path("/signAdmin")
    @RolesAllowed("Admin")
    public Response sign(final JsonObject j) {
        j.put("admin", "admin");
        return Respond.getSuccessResponse("sign",
            JsonObject.mapFrom(j));

    }

    @POST
    @Path("/signClient")
    @RolesAllowed("Client")
    public Response signClient(final JsonObject j) {
        j.put("client", "client");
        return Respond.getSuccessResponse("sign",
            JsonObject.mapFrom(j));

    }

    @POST
    @Path("/login")
    public Response loginUser(@Valid JsonObject xusr) {
        try {
            Users user = this.userInterface.findByEmail(
                    xusr.getString("email").toLowerCase());

            if (user.getAuthToken().equalsIgnoreCase(
                new HashPassword()
                .getHashToken(xusr.getString("password")))) {
                    JsonObject usr = JsonObject.mapFrom(user);
                usr.remove("authToken");
                return Respond.getSuccessResponse("login", usr);
            }
            JsonObject result = new JsonObject()
                .put("message", "Email or password is incorrect");
            return Respond.getSuccessResponse("login", result);
        } catch (final Exception e) {
            this.logger.error(e.getMessage(), e);
            return Respond.getErrorResponse("login", e);
        }
    }

    @POST
    @Path("/view-users")
    public Response view_loans() {
        logger.info("view-users -> ()");

        try {
            JsonObject loans = new JsonObject()
                .put("loans", this.userInterface.findAll());

            return Respond.getSuccessResponse("view-users", loans);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return Respond.getErrorResponse("view-users",
                e);
        }
    }
}