package com.zzangmin.gesipan.dao;

import com.zzangmin.gesipan.web.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {


    @Query("select u from Users u where u.userEmail=:email")
    Optional<Users> findByEmail(@Param("email") String email);
}
