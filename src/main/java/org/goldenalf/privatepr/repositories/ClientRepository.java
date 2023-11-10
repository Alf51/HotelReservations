package org.goldenalf.privatepr.repositories;

import org.goldenalf.privatepr.models.Client;
import org.goldenalf.privatepr.models.Hotel;
import org.goldenalf.privatepr.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByLogin(String login);
}
