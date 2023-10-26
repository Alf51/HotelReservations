package org.goldenalf.privatepr.repositories;

import org.goldenalf.privatepr.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepositories extends JpaRepository<Hotel, Integer> {
    Optional<Hotel> findByName(String name);
}
