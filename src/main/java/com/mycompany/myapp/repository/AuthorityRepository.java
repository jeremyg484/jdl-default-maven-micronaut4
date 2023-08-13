package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Authority;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

/**
 * Micronaut Data repository for the {@link Authority} entity.
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
