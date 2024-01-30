package org.example.rest_api_maven.repository;

import org.example.rest_api_maven.model.MataKuliah;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MataKuliahRepository extends JpaRepository<MataKuliah, Long> {
    Optional<MataKuliah> findByName(String name);
}

