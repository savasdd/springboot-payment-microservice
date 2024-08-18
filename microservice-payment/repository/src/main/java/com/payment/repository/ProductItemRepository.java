package com.payment.repository;

import com.payment.entity.model.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ProductItemRepository extends JpaRepository<ProductItem, UUID>, JpaSpecificationExecutor<ProductItem> {
}
