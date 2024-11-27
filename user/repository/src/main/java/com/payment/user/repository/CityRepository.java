package com.payment.user.repository;

import com.load.base.BaseRepository;
import com.payment.user.entity.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CityRepository extends JpaRepository<City, Long>, JpaSpecificationExecutor<City>, BaseRepository<City, Long> {

}
