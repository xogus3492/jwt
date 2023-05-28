package com.example.jwt.domain.user.domain.repository;

import com.example.jwt.domain.user.domain.User;
import com.example.jwt.global.security.CustomUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u.id, u.email, u.password, u.roleType " +
            "from User u " +
            "where u.id = :id")
    Optional<CustomUserDetails> findUserDetailsById(@Param("id") Long id);

}
