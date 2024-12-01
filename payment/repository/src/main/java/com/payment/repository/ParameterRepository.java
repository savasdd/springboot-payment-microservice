package com.payment.repository;

import com.payment.entity.model.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParameterRepository extends JpaRepository<Parameter, Long>{

    Optional<Parameter> findByKey(String key);
}
