package com.foxwear.productservice.repository;

import com.foxwear.productservice.entity.WearCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WearCategoryRepository extends JpaRepository<WearCategory, Long> {

    Optional<WearCategory> findByName(String name);

    Optional<WearCategory> findByLink(String link);

    boolean existsByName(String name);

    boolean existsByLink(String link);

    boolean existsByNameAndIdNot(String name, Long id);

    boolean existsByLinkAndIdNot(String link, Long id);

    List<WearCategory> findAllByParentIsNotNull();

}
