package com.payment.user.repository;

import com.load.base.BaseRepository;
import com.payment.user.entity.model.City;
import com.payment.user.entity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>, BaseRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
