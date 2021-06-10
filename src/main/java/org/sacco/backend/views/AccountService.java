package org.sacco.backend.views;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
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
import org.sacco.backend.interfaces.AccountsInterface;
import org.sacco.backend.interfaces.UserInterface;
import org.sacco.backend.models.Accounts;
import org.sacco.backend.models.HashPassword;
import org.sacco.backend.models.Role;
import org.sacco.backend.models.Users;
import org.sacco.backend.utils.Respond;
import org.sacco.backend.utils.TokenUtils;

import io.vertx.core.json.JsonObject;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountService {

    private static final Logger logger =
        Logger.getLogger(AccountService.class);

    private final UserInterface userInterface;
    private final AccountsInterface accountsInterface;

    public AccountService(final UserInterface uInterface,
        final AccountsInterface aInterface) {
        this.userInterface = uInterface;
        this.accountsInterface = aInterface;
    }


    @POST
    @Path("/deposit")
    @Transactional
    public Response deposit(@Valid JsonObject xusr) {
        logger.info("deposit -> ()");

        try {
            Users user =
                this.userInterface.findById(xusr.getLong("_id")).get();
            Accounts accs = new Accounts(user, xusr.getDouble("amount"));

            this.accountsInterface.save(accs);
            return Respond.getSuccessResponse("deposit",
                JsonObject.mapFrom(accs));
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return Respond.getErrorResponse("deposit", e);
        }
    }


    @POST
    @Path("/withdraw")
    @Transactional
    public Response withdraw(@Valid JsonObject xusr) {
        logger.info("withdraw -> ()");

        try {
            Accounts accs = this.accountsInterface
                .findById(xusr.getLong("_id")).get();

            if ((accs.getAmount()-xusr.getDouble("amount")) > 500.0) {
                accs.setAmount(accs.getAmount()-xusr.getDouble("amount"));
                this.accountsInterface.save(accs);
                return Respond.getSuccessResponse("withdraw",
                    JsonObject.mapFrom(accs));
            } else {
                return Respond.getErrorResponse("withdraw",
                    "Account has to remain with at least 500/=");
            }

        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return Respond.getErrorResponse("withdraw", e);
        }
    }

    @POST
    @Path("/view-accounts")
    @Transactional
    public Response view_loans() {
        logger.info("view-loans -> ()");

        try {
            JsonObject accounts = new JsonObject()
                .put("loans", this.accountsInterface.findAll());

            return Respond.getSuccessResponse("view-accounts", accounts);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return Respond.getErrorResponse("view-accounts",
                e);
        }
    }
}