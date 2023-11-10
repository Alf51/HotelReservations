package org.goldenalf.privatepr.repositories;

import org.goldenalf.privatepr.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByLogin(String login);
}
