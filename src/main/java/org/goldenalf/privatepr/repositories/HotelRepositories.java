package org.goldenalf.privatepr.repositories;

import org.goldenalf.privatepr.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepositories extends JpaRepository<Hotel, Integer> {
}
