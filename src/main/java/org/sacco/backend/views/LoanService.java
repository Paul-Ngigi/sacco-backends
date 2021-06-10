package org.sacco.backend.views;

import java.util.ArrayList;
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
import org.sacco.backend.interfaces.LoanInterface;
import org.sacco.backend.interfaces.UserInterface;
import org.sacco.backend.models.Accounts;
import org.sacco.backend.models.HashPassword;
import org.sacco.backend.models.Loan;
import org.sacco.backend.models.Role;
import org.sacco.backend.models.Users;
import org.sacco.backend.utils.Respond;
import org.sacco.backend.utils.TokenUtils;

import io.vertx.core.json.JsonObject;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanService {

    private static final Logger logger =
        Logger.getLogger(AccountService.class);

    private final UserInterface userInterface;
    private final AccountsInterface accountsInterface;
    private final LoanInterface loanInterface;

    public LoanService(final UserInterface uInterface,
        final LoanInterface lInterface,
        final AccountsInterface aInterface) {
        this.userInterface = uInterface;
        this.accountsInterface = aInterface;
        this.loanInterface = lInterface;
    }


    @POST
    @Path("/loan-apply")
    @Transactional
    public Response deposit(@Valid JsonObject xusr) {
        logger.info("loan-apply -> ()");

        try {
            List <Users> users = new ArrayList<>();
            Users user =
                this.userInterface.findById(xusr.getLong("_id")).get();

            for (int i = 0; i < xusr.getJsonArray("guaranters").size();
                i++) {
                JsonObject guaranter = xusr
                    .getJsonArray("guaranters")
                    .getJsonObject(i);

                    users.add(this.userInterface
                    .findById(guaranter.getLong("_id")).get());
                }

            Loan loan = new Loan(user,
                xusr.getDouble("amount"), users);
            if (loan.getAmount() <
                this.accountsInterface.findById(user.getId()).get().getAmount() * 3) {
                    this.loanInterface.save(loan);
                } else {
                    JsonObject result = new JsonObject()
                        .put("message", "Your loan limit is" +
                            this.accountsInterface
                                .findById(user.getId()).get().getAmount() * 3);

                    return Respond.getSuccessResponse("loan-apply",
                        result);
                }

            return Respond.getSuccessResponse("loan-apply",
                JsonObject.mapFrom(loan));
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return Respond.getErrorResponse("loan-apply", e);
        }
    }

    @POST
    @Path("/loan-approve")
    @Transactional
    public Response approve_loan(@Valid JsonObject xusr) {
        logger.info("loan-approve -> ()");

        try {
            Loan loan =
                this.loanInterface.findById(xusr.getLong("loan_id")).get();
            loan.setApproved(true);
            this.loanInterface.save(loan);
            Accounts accs =
                this.accountsInterface.findByUser(loan.getUser().getId());
            accs.setAmount(loan.getAmount());
            this.accountsInterface.save(accs);
            return Respond.getSuccessResponse("loan-approve",
                JsonObject.mapFrom(loan));
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return Respond.getErrorResponse("loan-approve", e);
        }
    }

    @POST
    @Path("/loan-disapprove")
    @Transactional
    public Response disapprove_loan(@Valid JsonObject xusr) {
        logger.info("loan-disapprove -> ()");

        try {
            Loan loan =
                this.loanInterface.findById(xusr.getLong("loan_id")).get();
            loan.setApproved(false);
            loan.setDisapproveReason(xusr.getString("reason", "NA"));
            this.loanInterface.save(loan);
            return Respond.getSuccessResponse("loan-disapprove",
                JsonObject.mapFrom(loan));
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return Respond.getErrorResponse("loan-disapprove", e);
        }
    }

    @POST
    @Path("/loan-pay")
    @Transactional
    public Response pay_loan(@Valid JsonObject xusr) {
        logger.info("loan-pay -> ()");

        try {
            Loan loan =
                this.loanInterface.findById(xusr.getLong("loan_id")).get();
            loan.setPaid(xusr.getDouble("amount"));
            loan.setFullyPaid();
            this.loanInterface.save(loan);
            return Respond.getSuccessResponse("loan-pay",
                JsonObject.mapFrom(loan));
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return Respond.getErrorResponse("loan-pay", e);
        }
    }

    @POST
    @Path("/view-loans")
    @Transactional
    public Response view_loans() {
        logger.info("view-loans -> ()");

        try {
            JsonObject loans = new JsonObject()
                .put("loans", this.loanInterface.findAll());

            return Respond.getSuccessResponse("view-loans", loans);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            return Respond.getErrorResponse("view-loans",
                e);
        }
    }
}