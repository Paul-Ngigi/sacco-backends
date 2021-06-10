package org.sacco.backend.interfaces;

import java.util.List;

import org.sacco.backend.models.Users;
import org.springframework.data.repository.CrudRepository;

public interface UserInterface extends CrudRepository<Users, Long> {

    Users findByEmail(String email);

    List<Users> findByUsername(String username);
}
