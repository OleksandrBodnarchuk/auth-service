package pl.alex.auth.user.persistable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface JpaUsersDao extends JpaRepository<UserEntity, UUID> {

    boolean existsByEmail(String email);
}