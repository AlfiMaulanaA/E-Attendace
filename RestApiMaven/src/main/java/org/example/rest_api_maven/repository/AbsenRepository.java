package org.example.rest_api_maven.repository;

import org.example.rest_api_maven.model.Absen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbsenRepository extends JpaRepository<Absen, Long> {
    // You can define custom query methods here if needed
}
