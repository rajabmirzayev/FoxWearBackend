package com.foxwear.orderservice.repository;

import com.foxwear.orderservice.entity.SaleItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    @Query("SELECT si.productItemId, SUM(si.quantity), SUM(si.totalPrice) " +
            "FROM SaleItem si " +
            "GROUP BY si.productItemId " +
            "ORDER BY SUM(si.quantity) DESC")
    List<Object[]> getTopSellingProductIdsFromSales(Pageable pageable);

}
