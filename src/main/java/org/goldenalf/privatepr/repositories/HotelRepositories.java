package org.goldenalf.privatepr.repositories;

import org.goldenalf.privatepr.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepositories extends JpaRepository<Hotel, Integer> {
}
