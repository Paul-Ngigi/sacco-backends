package org.sacco.backend.utils;

import java.io.FileInputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.internal.FirebaseCustomAuthToken;

import org.apache.commons.lang3.RandomStringUtils;
import org.sacco.backend.models.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FirebaseTransactions {

    /**
     * The logger instance that is used to log.
     */
    private Logger logger = LoggerFactory.getLogger(
        FirebaseTransactions.class.getName());

    private Firestore fStore;

    /**
     * The Firebase credentials.
     */
    public FirebaseTransactions() {
        this.initialiseFirebase();
        this.fStore = FirestoreClient.getFirestore();
    }

    public final void initialiseFirebase() {
        try {
            FileInputStream serviceAccount =
                new FileInputStream("/root/Documents/quarkus-projects/sacco/src/main/resources/serviceAccount.json");

            FirebaseOptions opt = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://sacco-quarkus-default-rtdb.europe-west1.firebasedatabase.app/")
                .build();

            FirebaseApp.initializeApp(opt);
        } catch (Exception e) {
            this.logger.warn("Error Initlialising firebase -> " + new JsonObject().put("exception", e.getLocalizedMessage()));
        }
    }

    public Firestore getFirestore() {
        return this.fStore;
    }

    public JsonObject addUserToFirebase(final JsonObject user) {
        if (user.getString("name") != null && user.getString("email") != null
            && user.getString("phone") != null && user.getString("pass") != null) {
            try {

                CreateRequest req = new CreateRequest()
                    .setDisabled(false)
                    .setDisplayName(user.getString("name"))
                    .setEmail(user.getString("email"))
                    .setEmailVerified(false)
                    .setDisabled(true)
                    .setPassword(user.getString("pass"))
                    .setPhoneNumber(user.getString("phone"));

                UserRecord urec = FirebaseAuth.getInstance().createUser(req);
                return user.put("password", user.getString("pass"))
                    .put("uid", urec.getUid())
                    .put("provider", urec.getProviderId());
            } catch (Exception e) {
                this.logger.error("Exception caught -> ", e);
                return null;
            }
        } else {
            return new JsonObject().put("error",
                "'name', 'email', 'phone' are required fields!");
        }
    }
}
