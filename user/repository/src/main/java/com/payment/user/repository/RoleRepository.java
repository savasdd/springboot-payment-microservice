package com.payment.user.repository;

import com.load.base.BaseRepository;
import com.payment.user.entity.model.City;
import com.payment.user.entity.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role>, BaseRepository<Role, Long> {
    List<Role> findAllByIdIn(List<Long> ids);
}
