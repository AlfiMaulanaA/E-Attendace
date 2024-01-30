package org.example.rest_api_maven.repository;

import org.example.rest_api_maven.model.MataKuliah;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MataKuliahRepository extends JpaRepository<MataKuliah, Long> {
    // You can define custom query methods here if needed
}
