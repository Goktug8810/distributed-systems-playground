package com.fsk.ecommerce.repository;

import com.fsk.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("""
                select u from User u
                left join fetch u.addresses
                left join fetch u.creditCards
                left join fetch u.hobbies
            """)
    List<User> findAllUserDetails();

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}


