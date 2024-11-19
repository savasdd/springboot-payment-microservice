package com.payment.repository;

import com.payment.entity.model.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductItemRepository extends JpaRepository<ProductItem, String>, JpaSpecificationExecutor<ProductItem> {
}
