package org.example.rest_api_maven.repository;

import org.example.rest_api_maven.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNim(Long nim);

}
