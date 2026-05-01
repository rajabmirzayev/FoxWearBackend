package com.foxwear.orderservice.repository;

import com.foxwear.orderservice.entity.Sale;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @NullMarked
    @EntityGraph(attributePaths = {"items"})
    Page<Sale> findAll(Pageable pageable);

}
