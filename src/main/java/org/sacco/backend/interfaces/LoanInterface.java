package org.sacco.backend.interfaces;

import org.sacco.backend.models.Loan;
import org.springframework.data.repository.CrudRepository;

public interface LoanInterface extends CrudRepository<Loan, Long> {


}
