package org.sacco.backend.interfaces;

import org.sacco.backend.models.Accounts;
import org.springframework.data.repository.CrudRepository;

public interface AccountsInterface
    extends CrudRepository <Accounts, Long> {
        Accounts findByUser(final Long user_id);
}
