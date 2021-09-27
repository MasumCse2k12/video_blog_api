package com.rokomari.videoapi.idm.repository;

import com.rokomari.videoapi.idm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "select * from users where username ilike ?1 limit 1", nativeQuery = true)
    Optional<User> findUserByUsername(String username);
    public User findByUsername(String username);
    @Query(value = "select * from users u where u.email ilike ?1  order by id asc limit 1", nativeQuery = true)
    public User findByEmail(String email);
    @Query(value = "select * from users u where u.phone ilike ?1  order by id asc limit 1", nativeQuery = true)
    public User findByPhone(String phone);
}
